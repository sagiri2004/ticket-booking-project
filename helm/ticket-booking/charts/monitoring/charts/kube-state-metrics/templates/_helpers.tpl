{{/*
Common labels
*/}}
{{- define "kube-state-metrics.labels" -}}
app: kube-state-metrics
{{- end -}}

{{/*
Selector labels
*/}}
{{- define "kube-state-metrics.selectorLabels" -}}
app: kube-state-metrics
{{- end -}}