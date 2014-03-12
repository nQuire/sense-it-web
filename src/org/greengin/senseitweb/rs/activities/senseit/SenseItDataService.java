package org.greengin.senseitweb.rs.activities.senseit;

import java.io.InputStream;
import java.util.Collection;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import org.codehaus.jackson.map.annotate.JsonView;
import org.greengin.senseitweb.entities.activities.senseit.SenseItSeries;
import org.greengin.senseitweb.json.mixins.Views;
import org.greengin.senseitweb.logic.data.NewDataItemResponse;
import org.greengin.senseitweb.logic.project.senseit.SenseItActivityActions;
import org.greengin.senseitweb.logic.project.senseit.SenseItSeriesManipulator;
import org.greengin.senseitweb.logic.voting.VoteCount;
import org.greengin.senseitweb.logic.voting.VoteRequest;

import com.sun.jersey.core.header.FormDataContentDisposition;
import com.sun.jersey.multipart.FormDataParam;

@Path("/project/{projectId}/senseit/data")
public class SenseItDataService {

	@GET
	@Produces("application/json")
	@JsonView({Views.VotableCount.class})
	public Collection<SenseItSeries> get(@PathParam("projectId") Long projectId, @Context HttpServletRequest request) {
		SenseItActivityActions member = new SenseItActivityActions(projectId, request);
		return member.getData();
	}

	
	@POST
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	@Produces(MediaType.APPLICATION_JSON)
	@JsonView({Views.VotableCount.class})
	public NewDataItemResponse<SenseItSeries> upload(@PathParam("projectId") Long projectId,
			@FormDataParam("title") String title,
			@FormDataParam("file") InputStream uploadedInputStream,
			@FormDataParam("file") FormDataContentDisposition fileDetail,
			@Context HttpServletRequest request) {
		
		SenseItActivityActions member = new SenseItActivityActions(projectId, request);
		return member.createData(new SenseItSeriesManipulator(title, uploadedInputStream));
	}

	@Path("/{dataId}")
	@DELETE
	@Consumes("application/json")
	@Produces("application/json")
	public Long delete(@PathParam("projectId") Long projectId, @PathParam("dataId") Long dataId, @Context HttpServletRequest request) {
		SenseItActivityActions member = new SenseItActivityActions(projectId, request);
		return member.deleteData(dataId, new SenseItSeriesManipulator(null, null));
	}
	
	@Path("/vote/{itemId}")
	@POST
	@Consumes("application/json")
	@Produces("application/json")
	@JsonView({Views.VotableCount.class})
	public VoteCount vote(@PathParam("projectId") Long projectId, @PathParam("itemId") Long itemId, VoteRequest voteData, @Context HttpServletRequest request) {
		SenseItActivityActions voter = new SenseItActivityActions(projectId, request);
		return voter.voteItem(itemId, voteData);
	}
	
	
	
	
}
