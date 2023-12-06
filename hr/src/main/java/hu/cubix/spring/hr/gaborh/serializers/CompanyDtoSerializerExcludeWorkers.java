package hu.cubix.spring.hr.gaborh.serializers;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import hu.cubix.spring.hr.gaborh.dto.CompanyDto;

public class CompanyDtoSerializerExcludeWorkers extends JsonSerializer<CompanyDto> {
	@Override
	public void serialize(CompanyDto company, JsonGenerator jsonGenerator, SerializerProvider serializerProvider)
			throws IOException {
		jsonGenerator.writeStartObject();
		jsonGenerator.writeStringField("id", company.getId());
		jsonGenerator.writeNumberField("registrationNumber", company.getRegistrationNumber());
		jsonGenerator.writeStringField("name", company.getName());
		jsonGenerator.writeStringField("address", company.getAddress());
		jsonGenerator.writeEndObject();
	}
}
