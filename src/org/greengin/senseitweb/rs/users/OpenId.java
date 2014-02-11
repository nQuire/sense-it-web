package org.greengin.senseitweb.rs.users;

import java.util.List;

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

public class OpenId {

	public enum Provider {
		GOOGLE, YAHOO;

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

	private static OpenId oi = new OpenId();

	public static OpenId instance() {
		return oi;
	}

	ConsumerManager manager;

	private OpenId() {
		manager = new ConsumerManager();
		manager.setAssociations(new InMemoryConsumerAssociationStore());
		manager.setNonceVerifier(new InMemoryNonceVerifier(5000));
	}
	
	public void logout(HttpServletRequest request) {
		HttpSession session = request.getSession();
		session.removeAttribute("openid");
        session.removeAttribute("openid-claimed");
	}
	
	public void redirect(Provider provider, HttpServletRequest request, HttpServletResponse response, ServletContext context) {
		HttpSession session = request.getSession();

		String identity = getMeIdentity(provider);

		String returnToUrl = "http://localhost/sense-it-web/login/response";

		try {
			List<?> discoveries = manager.discover(identity);
			DiscoveryInformation discovered = manager.associate(discoveries);

			// store the discovery information in the user's session
			session.setAttribute("openid-disco", discovered);

			// obtain a AuthRequest message to be sent to the OpenID provider
			AuthRequest authReq = manager.authenticate(discovered, returnToUrl);

			// Attribute Exchange example: fetching the 'email' attribute
			FetchRequest fetch = FetchRequest.createFetchRequest();
			fetch.addAttribute("email", "http://schema.openid.net/contact/email", true); 
			authReq.addExtension(fetch);
			
			if (!discovered.isVersion2()) {
				// Option 1: GET HTTP-redirect to the OpenID Provider endpoint
				// The only method supported in OpenID 1.x
				// redirect-URL usually limited ~2048 bytes
				response.sendRedirect(authReq.getDestinationUrl(true));
			} else {
				// Option 2: HTML FORM Redirection (Allows payloads >2048 bytes)

				RequestDispatcher dispatcher = context.getRequestDispatcher("/login/formredirection.jsp");
				request.setAttribute("parameterMap", request.getParameterMap());
				request.setAttribute("message", authReq);
				// httpReq.setAttribute("destinationUrl", httpResp
				// .getDestinationUrl(false));
				dispatcher.forward(request, response);
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
		return session.getAttribute("openid-email").toString();		
	}


	public void updateLoginStatus(HttpServletRequest request) {
		try {
			HttpSession session = request.getSession();

			ParameterList responselist = new ParameterList(
					request.getParameterMap());
			// retrieve the previously stored discovery information
			DiscoveryInformation discovered = (DiscoveryInformation) session
					.getAttribute("openid-disco");
			// extract the receiving URL from the HTTP request
			StringBuffer receivingURL = request.getRequestURL();
			String queryString = request.getQueryString();
			if (queryString != null && queryString.length() > 0)
				receivingURL.append("?").append(request.getQueryString());

			// verify the response; ConsumerManager needs to be the same
			// (static) instance used to place the authentication request
			VerificationResult verification = manager.verify(
					receivingURL.toString(), responselist, discovered);

			// examine the verification result and extract the verified
			// identifier
			Identifier verified = verification.getVerifiedId();
			if (verified != null) {
				AuthSuccess authSuccess = (AuthSuccess) verification
						.getAuthResponse();
				
				MessageExtension ext = authSuccess.getExtension(AxMessage.OPENID_NS_AX);
				
				session.setAttribute("openid", authSuccess.getIdentity());
				session.setAttribute("openid-claimed", authSuccess.getClaimed());

				if (ext instanceof FetchResponse) {
			        FetchResponse fetchResp = (FetchResponse) ext;
			        String email = fetchResp.getAttributeValue("email");
					session.setAttribute("openid-email", email);
			    }
				
				
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private String getMeIdentity(Provider provider) {
		switch (provider) {
		case YAHOO:
			return "https://me.yahoo.com";
		case GOOGLE:
		default:
			return "https://www.google.com/accounts/o8/id";
		}
	}
}
