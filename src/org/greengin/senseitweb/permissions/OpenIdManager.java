package org.greengin.senseitweb.permissions;

import java.util.List;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NameNotFoundException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.codehaus.jackson.annotate.JsonCreator;
import org.codehaus.jackson.annotate.JsonValue;
import org.openid4java.consumer.ConsumerManager;
import org.openid4java.consumer.InMemoryConsumerAssociationStore;
import org.openid4java.consumer.InMemoryNonceVerifier;
import org.openid4java.consumer.VerificationResult;
import org.openid4java.discovery.DiscoveryInformation;
import org.openid4java.discovery.Identifier;
import org.openid4java.message.AuthRequest;
import org.openid4java.message.AuthSuccess;
import org.openid4java.message.MessageExtension;
import org.openid4java.message.ParameterList;
import org.openid4java.message.ax.AxMessage;
import org.openid4java.message.ax.FetchRequest;
import org.openid4java.message.ax.FetchResponse;
import org.openid4java.util.HttpClientFactory;
import org.openid4java.util.ProxyProperties;

public class OpenIdManager {

	public enum Provider {
		OU, GOOGLE, YAHOO;

		@JsonValue
		public String getValue() {
			return this.name().toLowerCase();
		}

		@JsonCreator
		public static Provider create(String val) {
			Provider[] units = Provider.values();
			for (Provider unit : units) {
				if (unit.getValue().equals(val)) {
					return unit;
				}
			}
			return GOOGLE;
		}
	};

	private static OpenIdManager oi = new OpenIdManager();

	public static OpenIdManager instance() {
		return oi;
	}

	ConsumerManager manager;
	String realm;
	String returnUrl;

	private OpenIdManager() {
		try {
			Context env = (Context) (new InitialContext().lookup("java:comp/env"));
			this.realm = (String) env.lookup("serverBasePath");
			this.returnUrl = this.realm + (String) env.lookup("senseItLoginReturnPath");

			try {
				String serverHost = (String) env.lookup("serverProxyHostName");
				int serverPort = (Integer) env.lookup("serverProxyPort");

				ProxyProperties proxyProps = new ProxyProperties();
				proxyProps.setProxyHostName(serverHost);
				proxyProps.setProxyPort(serverPort);
				HttpClientFactory.setProxyProperties(proxyProps);

			} catch (NameNotFoundException e) {

			}

			manager = new ConsumerManager();
			manager.setAssociations(new InMemoryConsumerAssociationStore());
			manager.setNonceVerifier(new InMemoryNonceVerifier(5000));

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void logout(HttpServletRequest request) {
		HttpSession session = request.getSession();
		session.removeAttribute("openid");
		session.removeAttribute("openid-claimed");
	}

	public void redirect(Provider provider, HttpServletRequest request, HttpServletResponse response,
			ServletContext context) {

	}

	public void redirect(Provider provider, String username, HttpServletRequest request, HttpServletResponse response,
			ServletContext context) {

		String nextUrl;
		boolean requireId = false;

		switch (provider) {
		case OU:
			if (username == null) {
				requireId = true;
				nextUrl = "oulogin.jsp?p=ou";
			} else {
				nextUrl = "http://openid.open.ac.uk/oucu/" + username;
			}
			break;
		case YAHOO:
			nextUrl = "https://me.yahoo.com";
			break;
		case GOOGLE:
		default:
			nextUrl = "https://www.google.com/accounts/o8/id";
			break;
		}

		try {

			if (requireId) {
				response.sendRedirect(nextUrl);
			} else {

				HttpSession session = request.getSession();

				List<?> discoveries = manager.discover(nextUrl);
				DiscoveryInformation discovered = manager.associate(discoveries);

				// store the discovery information in the user's session
				session.setAttribute("openid-disco", discovered);

				// obtain a AuthRequest message to be sent to the OpenID
				// provider
				AuthRequest authReq = manager.authenticate(discovered, this.returnUrl, this.realm);

				// Attribute Exchange example: fetching the 'email' attribute
				FetchRequest fetch = FetchRequest.createFetchRequest();
				fetch.addAttribute("email", "http://schema.openid.net/contact/email", true);
				authReq.addExtension(fetch);

				if (!discovered.isVersion2()) {
					// Option 1: GET HTTP-redirect to the OpenID Provider
					// endpoint
					// The only method supported in OpenID 1.x
					// redirect-URL usually limited ~2048 bytes
					response.sendRedirect(authReq.getDestinationUrl(true));
				} else {
					// Option 2: HTML FORM Redirection (Allows payloads >2048
					// bytes)

					RequestDispatcher dispatcher = context.getRequestDispatcher("/login/formredirection.jsp");
					request.setAttribute("parameterMap", request.getParameterMap());
					request.setAttribute("message", authReq);
					// httpReq.setAttribute("destinationUrl", httpResp
					// .getDestinationUrl(false));
					dispatcher.forward(request, response);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public String getId(HttpServletRequest request) {
		HttpSession session = request.getSession();
		Object attr = session.getAttribute("openid");
		return attr != null ? attr.toString() : null;
	}

	public String getEmail(HttpServletRequest request) {
		HttpSession session = request.getSession();
		
		return (String) session.getAttribute("openid-email");
	}

	public void updateLoginStatus(HttpServletRequest request) {
		try {
			HttpSession session = request.getSession();

			ParameterList responselist = new ParameterList(request.getParameterMap());
			// retrieve the previously stored discovery information
			DiscoveryInformation discovered = (DiscoveryInformation) session.getAttribute("openid-disco");
			// extract the receiving URL from the HTTP request
			StringBuffer receivingURL = request.getRequestURL();
			String queryString = request.getQueryString();
			if (queryString != null && queryString.length() > 0)
				receivingURL.append("?").append(request.getQueryString());

			// verify the response; ConsumerManager needs to be the same
			// (static) instance used to place the authentication request
			VerificationResult verification = manager.verify(receivingURL.toString(), responselist, discovered);

			// examine the verification result and extract the verified
			// identifier
			Identifier verified = verification.getVerifiedId();
			if (verified != null) {
				AuthSuccess authSuccess = (AuthSuccess) verification.getAuthResponse();

				session.setAttribute("openid", authSuccess.getIdentity());
				session.setAttribute("openid-claimed", authSuccess.getClaimed());

				try {
					MessageExtension ext = authSuccess.getExtension(AxMessage.OPENID_NS_AX);

					if (ext instanceof FetchResponse) {
						FetchResponse fetchResp = (FetchResponse) ext;
						String email = fetchResp.getAttributeValue("email");
						session.setAttribute("openid-email", email);
					}
				} catch (Exception e) {
					session.setAttribute("openid-email", null);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
