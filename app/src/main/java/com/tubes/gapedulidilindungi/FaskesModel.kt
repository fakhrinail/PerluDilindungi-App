package com.tubes.gapedulidilindungi

import com.google.gson.annotations.SerializedName

data class FaskesModel(
    val success: Boolean,
    val message: String,
    val count_total: Int,
    val results: List<FasksesResults>
)

data class FasksesResults(
    @SerializedName("id") var id: Int? = null,
    @SerializedName("kode") var kode: String? = null,
    @SerializedName("nama") var nama: String? = null,
    @SerializedName("kota") var kota: String? = null,
    @SerializedName("provinsi") var provinsi: String? = null,
    @SerializedName("alamat") var alamat: String? = null,
    @SerializedName("latitude") var latitude: String? = null,
    @SerializedName("longitude") var longitude: String? = null,
    @SerializedName("telp") var telp: String? = null,
    @SerializedName("jenis_faskes") var jenisFaskes: String? = null,
    @SerializedName("kelas_rs") var kelasRs: String? = null,
    @SerializedName("status") var status: String? = null,
    @SerializedName("detail") var detail: ArrayList<FaskesDetail> = arrayListOf(),
    @SerializedName("source_data") var sourceData: String? = null
)

data class FaskesDetail(
    @SerializedName("id") var id: Int? = null,
    @SerializedName("kode") var kode: String? = null,
    @SerializedName("batch") var batch: String? = null,
    @SerializedName("divaksin") var divaksin: Int? = null,
    @SerializedName("divaksin_1") var divaksin1: Int? = null,
    @SerializedName("divaksin_2") var divaksin2: Int? = null,
    @SerializedName("batal_vaksin") var batalVaksin: Int? = null,
    @SerializedName("batal_vaksin_1") var batalVaksin1: Int? = null,
    @SerializedName("batal_vaksin_2") var batalVaksin2: Int? = null,
    @SerializedName("pending_vaksin") var pendingVaksin: Int? = null,
    @SerializedName("pending_vaksin_1") var pendingVaksin1: Int? = null,
    @SerializedName("pending_vaksin_2") var pendingVaksin2: Int? = null,
    @SerializedName("tanggal") var tanggal: String? = null
)