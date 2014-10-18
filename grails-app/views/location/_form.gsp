<%@ page import="inauthcodingchallange.Location" %>



<div class="fieldcontain ${hasErrors(bean: locationInstance, field: 'latitude', 'error')} required">
	<label for="latitude">
		<g:message code="location.latitude.label" default="Latitude" />
		<span class="required-indicator">*</span>
	</label>
	<g:field name="latitude" value="${fieldValue(bean: locationInstance, field: 'latitude')}" required=""/>

</div>

<div class="fieldcontain ${hasErrors(bean: locationInstance, field: 'longitude', 'error')} required">
	<label for="longitude">
		<g:message code="location.longitude.label" default="Longitude" />
		<span class="required-indicator">*</span>
	</label>
	<g:field name="longitude" value="${fieldValue(bean: locationInstance, field: 'longitude')}" required=""/>

</div>

