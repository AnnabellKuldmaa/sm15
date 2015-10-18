package parsers;

import java.util.Date;
import org.deckfour.xes.extension.std.XConceptExtension;
import org.deckfour.xes.extension.std.XLifecycleExtension;
import org.deckfour.xes.extension.std.XTimeExtension;
import org.deckfour.xes.model.XAttributeMap;
import org.deckfour.xes.model.XEvent;
import org.deckfour.xes.model.XLog;
import org.deckfour.xes.model.XTrace;

public class XESTest {

	public static void main(String[] args) {
		try {
			XLog log = XLogReader.openLog("test.xes");
			// Loop traces in a log
			for (XTrace trace : log) {
				String traceName = XConceptExtension.instance().extractName(
						trace);
				XAttributeMap caseAttributes = trace.getAttributes();

				for (XEvent event : trace) {
					String activityName = XConceptExtension.instance()
							.extractName(event); // Event name
					Date timestamp = XTimeExtension.instance()
							.extractTimestamp(event); // Event timestamp
					String eventType = XLifecycleExtension.instance()
							.extractTransition(event); // EventType
					// System.out.println(activityName + timestamp + eventType);
					// // A Thu Sep 24 09:35:11 EEST 2009 complete
					XAttributeMap eventAttributes = event.getAttributes();
					for (String key : eventAttributes.keySet()) {
						String value = eventAttributes.get(key).toString();
						/*
						 * Sama, mis eelmine? A /complete 3
						 * 2009-09-24T09:35:11.100+03:00
						 */
						// System.out.println(value);
					}
					for (String key : caseAttributes.keySet()) {
						String value = caseAttributes.get(key).toString();
						// Mis jama see veel on?
						// System.out.println(value);
					}
				}

			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
