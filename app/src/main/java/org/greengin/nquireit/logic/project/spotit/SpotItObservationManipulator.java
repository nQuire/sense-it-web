package org.greengin.nquireit.logic.project.spotit;

import org.greengin.nquireit.entities.activities.spotit.SpotItActivity;
import org.greengin.nquireit.entities.activities.spotit.SpotItObservation;
import org.greengin.nquireit.entities.projects.Project;
import org.greengin.nquireit.logic.ContextBean;
import org.greengin.nquireit.logic.data.DataItemManipulator;
import org.greengin.nquireit.logic.files.FileMapUpload;
import org.greengin.nquireit.logic.project.senseit.UpdateTitleRequest;

import java.io.IOException;

public class SpotItObservationManipulator extends DataItemManipulator<SpotItActivity, SpotItObservation> {

    SpotItObservationRequest data;
    FileMapUpload.FileData file;
    ContextBean context;
    UpdateTitleRequest title;

	public SpotItObservationManipulator(ContextBean context, SpotItObservationRequest data, FileMapUpload.FileData file, UpdateTitleRequest title) {
        this.data = data;
		this.file = file;
        this.context = context;
        this.title = title;
	}

	@Override
	public boolean onCreate(SpotItObservation newItem) {
        try {
            String filename = context.getFileManager().uploadFile(project.getId().toString(), file.filename, file.data);
            newItem.setObservation(filename);
            newItem.setTitle(data.getTitle());
            newItem.setGeolocation(data.getGeolocation());
            return true;
        } catch (IOException e) {
            return false;
        }
	}

	@Override
	public void onUpdate(SpotItObservation item) {
        if (title != null) {
            item.setTitle(title.getTitle());
        }
	}

	@Override
	public void onDelete(SpotItObservation item) {
	}

}
