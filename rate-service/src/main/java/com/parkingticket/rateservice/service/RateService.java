package com.parkingticket.rateservice.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.parkingticket.rateservice.entity.RateConfig;
import com.parkingticket.rateservice.repository.RateRepository;

@Service
public class RateService {
	@Autowired
	private RateRepository repo;

	public List<RateConfig> findAll() {
		return repo.findAll();
	}

	public RateConfig current() {
		// first record is current, create one if it doesn't exist
		List<RateConfig> list = repo.findAll();
		if (list.isEmpty()) {
			RateConfig def = new RateConfig();
			return repo.save(def);
		}
		return list.get(0);
	}

	public RateConfig update(RateConfig data) {
		RateConfig cur = current();
		if (data.getPricingModel() != null)
			cur.setPricingModel(data.getPricingModel());
		if (data.getPerHour() != null)
			cur.setPerHour(data.getPerHour());
		if (data.getGraceMinutes() != null)
			cur.setGraceMinutes(data.getGraceMinutes());
		if (data.getRoundUpToHour() != null)
			cur.setRoundUpToHour(data.getRoundUpToHour());
		if (data.getFlatAmount() != null)
			cur.setFlatAmount(data.getFlatAmount());
		return repo.save(cur);
	}
}
