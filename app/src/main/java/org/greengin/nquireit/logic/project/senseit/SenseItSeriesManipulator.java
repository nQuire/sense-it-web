package org.greengin.nquireit.logic.project.senseit;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Vector;

import org.greengin.nquireit.entities.activities.senseit.SenseItActivity;
import org.greengin.nquireit.entities.activities.senseit.SenseItProfile;
import org.greengin.nquireit.entities.activities.senseit.SenseItSeries;
import org.greengin.nquireit.entities.activities.senseit.SensorInput;
import org.greengin.nquireit.entities.projects.Project;
import org.greengin.nquireit.logic.data.DataItemManipulator;
import org.greengin.nquireit.utils.TimeValue;

public class SenseItSeriesManipulator implements DataItemManipulator<SenseItActivity, SenseItSeries> {

	String title;
	String geolocation;
	InputStream uploadedInputStream;

	public SenseItSeriesManipulator(String title, String geolocation, InputStream uploadedInputStream) {
		this.title = title;
		this.geolocation = geolocation;
		this.uploadedInputStream = uploadedInputStream;
	}

	@Override
	public boolean onCreate(Project project, SenseItActivity activity, SenseItSeries newItem) {
		SenseItProfile profile = activity.getProfile();
		newItem.setTitle(title);
		newItem.setGeolocation(geolocation);

		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(uploadedInputStream));
			boolean valid = false;

			HashMap<Long, String> sensors = newItem.getSensors();
			sensors.clear();
			HashMap<Long, Vector<TimeValue>> data = newItem.getData();
			data.clear();

			while (true) {
				String line = reader.readLine();

				if (line == null) {
					break;
				}

				if (line.startsWith("#")) {
					String[] parts = line.split(" ", 5);
					if (parts.length < 2) {
						return false;
					}

					if ("profile:".equals(parts[1])) {
						if (valid || parts.length != 3) {
							return false;
						}

						String[] idparts = parts[2].split("\\.");
						if (idparts.length != 3 || !"r".equals(idparts[0]) || Long.parseLong(idparts[1]) != project.getId()
								&& Long.parseLong(idparts[2]) != profile.getId()) {
							return false;
						}

						valid = true;
					} else if ("sensor:".equals(parts[1])) {
						if (!valid || parts.length != 5) {
							return false;
						}

						long sensorInputId = Long.parseLong(parts[2]);
						String type = parts[3].split(":")[0];
						String name = parts[4];
						SensorInput input = profile.inputById(sensorInputId);

						if (input == null || !type.equals(input.getSensor()) || 
								sensors.containsKey(sensorInputId) || name.length() == 0) {
							return false;
						}

						
						sensors.put(sensorInputId, name);
						data.put(sensorInputId, new Vector<TimeValue>());
					}
				} else {
					String[] parts = line.split(",");
					if (!valid || parts.length < 3) {
						return false;
					}

					long sensorInputId = Long.parseLong(parts[0]);
					if (!sensors.containsKey(sensorInputId)) {
						return false;
					}

					long time = Long.parseLong(parts[1]);
					float[] values = new float[parts.length - 2];
					for (int i = 2; i < parts.length; i++) {
						values[i - 2] = Float.parseFloat(parts[i]);
					}
					data.get(sensorInputId).add(new TimeValue(time, values));
				}
			}

			reader.close();
			return valid;

		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public void onUpdate(Project project, SenseItActivity activity, SenseItSeries item) {
	}

	@Override
	public void onDelete(Project project, SenseItActivity activity, SenseItSeries item) {
	}

}
