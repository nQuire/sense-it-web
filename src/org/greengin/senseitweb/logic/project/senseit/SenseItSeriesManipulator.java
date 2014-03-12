package org.greengin.senseitweb.logic.project.senseit;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Vector;

import org.greengin.senseitweb.entities.activities.senseit.SenseItActivity;
import org.greengin.senseitweb.entities.activities.senseit.SenseItProfile;
import org.greengin.senseitweb.entities.activities.senseit.SenseItSeries;
import org.greengin.senseitweb.entities.activities.senseit.SensorInput;
import org.greengin.senseitweb.entities.projects.Project;
import org.greengin.senseitweb.logic.data.DataItemManipulator;
import org.greengin.senseitweb.utils.TimeValue;

public class SenseItSeriesManipulator implements DataItemManipulator<SenseItActivity, SenseItSeries> {

	String title;
	InputStream uploadedInputStream;

	public SenseItSeriesManipulator(String title, InputStream uploadedInputStream) {
		this.title = title;
		this.uploadedInputStream = uploadedInputStream;
	}

	@Override
	public boolean onCreate(Project project, SenseItActivity activity, SenseItSeries newItem) {
		SenseItProfile profile = activity.getProfile();
		newItem.setTitle(title);

		try {

			BufferedReader reader = new BufferedReader(new InputStreamReader(uploadedInputStream));
			boolean valid = false;

			HashMap<Long, String> sensors = newItem.getSensors();
			sensors.clear();
			HashMap<Long, Vector<TimeValue>> data = newItem.getData();
			data.clear();
			HashMap<Long, Long> sensorInputIds = new HashMap<Long, Long>();

			while (true) {
				String line = reader.readLine();

				if (line == null) {
					break;
				}

				if (line.startsWith("#")) {
					String[] parts = line.split(" ", 6);
					if (parts.length < 2) {
						return false;
					}

					if ("profile:".equals(parts[1])) {
						valid = !valid && parts.length == 4 && Long.parseLong(parts[2]) == project.getId()
								&& Long.parseLong(parts[3]) == profile.getId();
						
						if (!valid) {
							return false;
						}
					} else if ("sensor:".equals(parts[1])) {
						if (!valid || parts.length != 6) {
							return false;
						}

						long sensorInputId = Long.parseLong(parts[2]);
						long csvSensorId = Long.parseLong(parts[3]);
						String type = parts[4].split(":")[0];
						String name = parts[5];
						SensorInput input = profile.inputById(sensorInputId);

						if (input == null || !type.equals(input.getSensor()) || sensorInputIds.containsKey(csvSensorId)
								|| sensors.containsKey(sensorInputIds) || name.length() == 0) {
							return false;
						}

						sensorInputIds.put(csvSensorId, sensorInputId);
						sensors.put(sensorInputId, name);
						data.put(sensorInputId, new Vector<TimeValue>());

					}
				} else {
					String[] parts = line.split(",");
					if (!valid || parts.length < 3) {
						return false;
					}

					long csvSensorId = Long.parseLong(parts[0]);
					if (!sensorInputIds.containsKey(csvSensorId)) {
						return false;
					}

					long sensorInputId = sensorInputIds.get(csvSensorId);

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
