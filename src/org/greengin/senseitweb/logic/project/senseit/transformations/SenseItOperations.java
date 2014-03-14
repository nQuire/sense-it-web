package org.greengin.senseitweb.logic.project.senseit.transformations;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Vector;

import javax.servlet.ServletContext;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.greengin.senseitweb.entities.activities.senseit.SenseItActivity;
import org.greengin.senseitweb.entities.activities.senseit.SenseItTransformation;
import org.greengin.senseitweb.entities.activities.senseit.SensorInput;
import org.greengin.senseitweb.logic.project.senseit.transformations.maths.Abs;
import org.greengin.senseitweb.logic.project.senseit.transformations.maths.Derivative;
import org.greengin.senseitweb.logic.project.senseit.transformations.maths.FilterCC;
import org.greengin.senseitweb.logic.project.senseit.transformations.maths.GetX;
import org.greengin.senseitweb.logic.project.senseit.transformations.maths.GetY;
import org.greengin.senseitweb.logic.project.senseit.transformations.maths.GetZ;
import org.greengin.senseitweb.logic.project.senseit.transformations.maths.Integrate;
import org.greengin.senseitweb.logic.project.senseit.transformations.maths.Max;
import org.greengin.senseitweb.logic.project.senseit.transformations.maths.Min;
import org.greengin.senseitweb.utils.TimeValue;

public class SenseItOperations {
	private static final HashMap<String, SenseItOperation> ops;
	static {
		ops = new HashMap<String, SenseItOperation>();
		ops.put("integrate", new Integrate());
		ops.put("derivative", new Derivative());
		ops.put("modulus", null);
		ops.put("abs", new Abs());
		ops.put("getx", new GetX());
		ops.put("gety", new GetY());
		ops.put("getz", new GetZ());
		ops.put("removecc", new FilterCC());
		ops.put("max", new Max());
		ops.put("min", new Min());
	}

	private static SenseItData data = null;

	public static void init(ServletContext context) {
		if (data == null) {
			data = loadData(context);
		}
	}

	private static SenseItData loadData(ServletContext context) {
		ObjectMapper mapper = new ObjectMapper();
		try {
			return mapper.readValue(context.getResourceAsStream("/WEB-INF/data/senseit.json"), SenseItData.class);
		} catch (JsonParseException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return null;
	}

	public static SenseItData get() {
		return data;
	}

	public static SenseItProcessedSeries process(HashMap<Long, Vector<TimeValue>> series, SenseItActivity activity) {
		int index = 0;
		SenseItProcessedSeries processed = new SenseItProcessedSeries();

		for (Entry<Long, Vector<TimeValue>> entry : series.entrySet()) {
			SensorInput input = activity.getProfile().inputById(entry.getKey());
			if (input != null) {
				SenseItDataSensor sensorData = data.sensorTypes.get(input.getSensor());
				if (sensorData != null) {
					SenseItProcessedSeriesVariable var = new SenseItProcessedSeriesVariable(index++, entry.getValue());
					var.units.apply(sensorData.units);
					processed.values.put(String.valueOf(entry.getKey()), var);
				}
			}
		}

		boolean goon = true;
		while (goon) {
			goon = false;
			for (SenseItTransformation tx : activity.getProfile().getTx()) {
				String varId = tx.getId();
				if (processed.values.get(varId) == null) {
					System.out.println(varId + " " + tx.getType());
					SenseItOperation op = ops.get(tx.getType());
					if (op != null) {
						SenseItProcessedSeriesVariable input = null;
						Vector<Vector<TimeValue>> inputs = new Vector<Vector<TimeValue>>();
						boolean good = true;
						for (String inputId : tx.getInputs()) {
							input = processed.values.get(inputId);
							if (input == null) {
								good = false;
								break;
							} else {
								inputs.add(input.values);
							}
						}

						if (good) {
							SenseItProcessedSeriesVariable result = new SenseItProcessedSeriesVariable(index++);
							if (op.process(inputs, result.values)) {
								SenseItDataTransformation txData = data.transformations.get(tx.getType());
								if (input != null) {
									result.units.apply(input.units);
								}
								result.units.apply(txData.getUnits());
								processed.values.put(varId, result);
								goon = true;
							}
						}
					}
				}
			}
		}

		return processed;
	}

	public static HashMap<String, TimeValue> tableVariables(SenseItProcessedSeries data, SenseItActivity activity) {
		HashMap<String, TimeValue> values = new HashMap<String, TimeValue>();
		for (SenseItTransformation tx : activity.getProfile().getTx()) {
			String type = tx.getType();
			if ("max".equals(type) || "min".equals(type)) {
				String id = tx.getId();
				SenseItProcessedSeriesVariable var = data.values.get(id);
				values.put(id, var != null && var.values.size() > 0 ? var.values.firstElement() : null);
			}
		}

		return values;
	}

}
