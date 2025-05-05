{{/*
Common labels for Prometheus
*/}}
{{- define "prometheus.labels" -}}
app: prometheus
{{- end -}}

{{/*
Selector labels for Prometheus
*/}}
{{- define "prometheus.selectorLabels" -}}
app: prometheus
{{- end -}}

{{/*
Common labels for Grafana
*/}}
{{- define "grafana.labels" -}}
app: grafana
{{- end -}}

{{/*
Selector labels for Grafana
*/}}
{{- define "grafana.selectorLabels" -}}
app: grafana
{{- end -}}