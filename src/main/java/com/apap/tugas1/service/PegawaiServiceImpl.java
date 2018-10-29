package com.apap.tugas1.service;

import java.sql.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.apap.tugas1.model.InstansiModel;
import com.apap.tugas1.model.JabatanModel;
import com.apap.tugas1.model.PegawaiModel;
import com.apap.tugas1.model.ProvinsiModel;
import com.apap.tugas1.repository.PegawaiDb;

@Service
@Transactional
public class PegawaiServiceImpl implements PegawaiService {
	@Autowired
	private PegawaiDb pegawaiDb;
	
	@Override
	public PegawaiModel getPegawaiDetailByNip(String nip) {
		return pegawaiDb.findByNip(nip);
	}
	
	@Override
	public double generateGaji(String nip) {
		PegawaiModel pegawai = this.getPegawaiDetailByNip(nip);
		double presentaseTunjangan = pegawai.getInstansi().getProvinsi().getPresentase_tunjangan();
		double gajiMax = 0;
		for (JabatanModel jabatan : pegawai.getJabatanList()) {
			if (jabatan.getGaji_pokok() > gajiMax ) {
				gajiMax = jabatan.getGaji_pokok();
			}
		}
		double gajiTotal = gajiMax + ((presentaseTunjangan/100) * gajiMax);
		return gajiTotal;

	}

	@Override
	public PegawaiModel getPegawaiTertua(List<PegawaiModel> pegawaiList) {
		PegawaiModel pegawaiTertua = null;
		
		for (PegawaiModel pegawai: pegawaiList) {
			if (pegawaiTertua == null) {
				pegawaiTertua = pegawai;
			}
			else {
				if (pegawai.getTanggalLahir().compareTo(pegawaiTertua.getTanggalLahir()) < 0) {
					pegawaiTertua = pegawai;
				}
			}
		}

		return pegawaiTertua;
	}

	@Override
	public PegawaiModel getPegawaiTermuda(List<PegawaiModel> pegawaiList) {
		PegawaiModel pegawaiTermuda = null;
		
		for (PegawaiModel pegawai: pegawaiList) {
			if (pegawaiTermuda == null) {
				pegawaiTermuda = pegawai;
			}
			else {
				if (pegawai.getTanggalLahir().compareTo(pegawaiTermuda.getTanggalLahir()) > 0) {
					pegawaiTermuda = pegawai;
				}
			}
		}
		return pegawaiTermuda;
	}

	@Override
	  public String generateNip(PegawaiModel pegawai) {
	  String kodeInstansi = String.valueOf(pegawai.getInstansi().getId());
	  String[] dateSplit = String.valueOf(pegawai.getTanggalLahir().toString()).split("-");
	  String tahun = dateSplit[0];
	  String bulan = dateSplit[1];
	  String tanggal = dateSplit[2];
	  tahun = tahun.substring(2, 4);
	  String tahunMasuk = pegawai.getTahunMasuk();
	   
	  // generate nomor urut
	  int noUrut;
	  List<PegawaiModel> listPegawai = pegawaiDb.findByInstansiAndTanggalLahirAndTahunMasukOrderByNipAsc(pegawai.getInstansi(), pegawai.getTanggalLahir(), pegawai.getTahunMasuk());
	  if(listPegawai.size() == 0) {
	   noUrut = 1;
	   }
	  else {
		  PegawaiModel lastPegawai = listPegawai.get(listPegawai.size() - 1);
		  noUrut =Integer.parseInt(lastPegawai.getNip().substring(14, 16)) + 1;
	   }
	  String noUrutStr;
	  if(noUrut == 0) {
		  noUrutStr = "01";
	   }
	  else if (noUrut < 10) {
		  noUrutStr = "0";
		  noUrutStr += String.valueOf(noUrut);
	   }
	  else {
		  noUrutStr = String.valueOf(noUrut);
	   }
	   // END generate nomor urut
	  String nip = kodeInstansi + tanggal + bulan + tahun + tahunMasuk + noUrutStr;
	  return nip;
	  }

	@Override
	public void addPegawai(PegawaiModel pegawai) {
		pegawaiDb.save(pegawai);
	}

	@Override
	public List<PegawaiModel> getListPegawai() {
		return pegawaiDb.findAll();
	}

}