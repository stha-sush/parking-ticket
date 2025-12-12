package com.parkingticket.ticket_service.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class AuthResp {

	private int respCode;
	private String respDesc;
	private String token;

}