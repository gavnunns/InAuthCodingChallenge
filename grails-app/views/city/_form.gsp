<%@ page import="inauthcodingchallange.City" %>



<div class="fieldcontain ${hasErrors(bean: cityInstance, field: 'latitude', 'error')} required">
	<label for="latitude">
		<g:message code="city.latitude.label" default="Latitude" />
		<span class="required-indicator">*</span>
	</label>
	<g:field name="latitude" value="${fieldValue(bean: cityInstance, field: 'latitude')}" required=""/>

</div>

<div class="fieldcontain ${hasErrors(bean: cityInstance, field: 'longitude', 'error')} required">
	<label for="longitude">
		<g:message code="city.longitude.label" default="Longitude" />
		<span class="required-indicator">*</span>
	</label>
	<g:field name="longitude" value="${fieldValue(bean: cityInstance, field: 'longitude')}" required=""/>

</div>

<div class="fieldcontain ${hasErrors(bean: cityInstance, field: 'name', 'error')} required">
	<label for="name">
		<g:message code="city.name.label" default="Name" />
		<span class="required-indicator">*</span>
	</label>
	<g:textField name="name" required="" value="${cityInstance?.name}"/>

</div>

