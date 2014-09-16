package org.greengin.nquireit.logic.project.spotit;

import org.greengin.nquireit.entities.activities.spotit.SpotItActivity;
import org.greengin.nquireit.entities.activities.spotit.SpotItObservation;
import org.greengin.nquireit.logic.ContextBean;
import org.greengin.nquireit.logic.data.DataItemManipulator;
import org.greengin.nquireit.logic.files.FileMapUpload;

import java.io.IOException;

public class SpotItObservationManipulator extends DataItemManipulator<SpotItActivity, SpotItObservation> {

    SpotItObservationRequest data;
    FileMapUpload.FileData file;
    ContextBean context;
    UpdateImageRequest metadata;

	public SpotItObservationManipulator(ContextBean context, SpotItObservationRequest data, FileMapUpload.FileData file, UpdateImageRequest metadata) {
        this.data = data;
		this.file = file;
        this.context = context;
        this.metadata = metadata;
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
        if (metadata != null) {
            if (metadata.getTitle() != null) {
                item.setTitle(metadata.getTitle());
            }

            if (metadata.getRotate() != null) {
                context.getFileManager().rotateImage(item.getObservation(), metadata.getRotate());
            }
        }
	}

	@Override
	public void onDelete(SpotItObservation item) {
	}

}
