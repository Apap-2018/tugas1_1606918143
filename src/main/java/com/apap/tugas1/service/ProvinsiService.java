package com.apap.tugas1.service;

import java.util.List;

import com.apap.tugas1.model.ProvinsiModel;
import com.apap.tugas1.repository.ProvinsiDb;

public interface ProvinsiService {
	List<ProvinsiModel> getAllProvinsi();

	ProvinsiModel getProvinsiDetailById(long idProvinsi);
	
	public List<ProvinsiModel> getListProvinsi();
}
