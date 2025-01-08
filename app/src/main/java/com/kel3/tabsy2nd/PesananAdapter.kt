package com.kel3.tabsy2nd

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.FirebaseDatabase
import com.kel3.tabsy2nd.R
import com.kel3.tabsy2nd.Pesanan

class PesananAdapter(
    private val pesananList: List<Pesanan>,
    private val onCancelClicked: (Pesanan) -> Unit,
    private val onHubungiClicked: (Pesanan) -> Unit
) : RecyclerView.Adapter<PesananAdapter.PesananViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PesananViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_pesanan, parent, false)
        return PesananViewHolder(view)
    }

    override fun onBindViewHolder(holder: PesananViewHolder, position: Int) {
        val pesanan = pesananList[position]
        holder.bind(pesanan)
    }

    override fun getItemCount(): Int = pesananList.size

    inner class PesananViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvNamaRestoran: TextView = itemView.findViewById(R.id.tvNamaRestoran)
        private val tvTanggalWaktu: TextView = itemView.findViewById(R.id.tvTanggalWaktu)
        private val tvJumlahOrang: TextView = itemView.findViewById(R.id.tvJumlahOrang)
        private val tvStatusPesanan: TextView = itemView.findViewById(R.id.tvStatusPesanan)
        private val btnCancelPesanan: Button = itemView.findViewById(R.id.btnCancelPesanan)
        private val btnHubungiRestoran: Button = itemView.findViewById(R.id.btnHubungiRestoran)

        fun bind(pesanan: Pesanan) {
            tvNamaRestoran.text = pesanan.idResto // Ini bisa diganti dengan nama restoran jika ada data detail
            tvTanggalWaktu.text = "${pesanan.tanggal} - ${pesanan.waktu}"
            tvJumlahOrang.text = "Jumlah Orang: ${pesanan.jumlahOrang}"
            tvStatusPesanan.text = "Status: ${pesanan.status}"

            btnCancelPesanan.setOnClickListener { onCancelClicked(pesanan) }
            btnHubungiRestoran.setOnClickListener { onHubungiClicked(pesanan) }
        }
    }
}
