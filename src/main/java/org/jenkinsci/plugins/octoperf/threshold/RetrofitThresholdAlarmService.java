package org.jenkinsci.plugins.octoperf.threshold;


import org.jenkinsci.plugins.octoperf.client.RestApiFactory;

import java.io.IOException;

import static java.lang.String.format;
import static okhttp3.MediaType.parse;
import static okhttp3.RequestBody.create;

final class RetrofitThresholdAlarmService implements ThresholdAlarmService {
  private static final String THRESHOLD_REPORT_ITEM =
    "{\"@type\":\"ThresholdAlarmReportItem\",\"metric\":{\"@type\":\"MonitoringMetric\",\"benchResultId\":\"%s\", \"filters\":[],\"id\":\"\",\"type\":\"NUMBER_COUNTER\"},\"name\":\"\"}";

  public boolean hasAlarms(
    final RestApiFactory apiFactory,
    final String benchResultId,
    final ThresholdSeverity severity) throws IOException {
    final ThresholdAlarmApi api = apiFactory.create(ThresholdAlarmApi.class);

    return api
      .getAlarms(create(parse("application/json"), format(THRESHOLD_REPORT_ITEM, benchResultId)))
      .execute()
      .body()
      .stream()
      .map(ThresholdAlarm::getSeverity)
      .anyMatch(severity::equals);
  }
}
