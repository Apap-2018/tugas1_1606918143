package com.apap.tugas1.service;

import java.sql.Date;
import java.util.List;

import com.apap.tugas1.model.InstansiModel;
import com.apap.tugas1.model.JabatanModel;
import com.apap.tugas1.model.PegawaiModel;

public interface PegawaiService {
	PegawaiModel getPegawaiDetailByNip(String nip);
	
	double generateGaji(String nip);
	
	PegawaiModel getPegawaiTertua(List<PegawaiModel> pegawaiList);
	
	PegawaiModel getPegawaiTermuda(List<PegawaiModel> pegawaiList);

	String generateNip(PegawaiModel pegawai);

	void addPegawai(PegawaiModel pegawai);
	
	public List<PegawaiModel> getListPegawai();
	
}
