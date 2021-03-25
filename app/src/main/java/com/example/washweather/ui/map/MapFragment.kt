package com.example.washweather.ui.map

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.washweather.R
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class MapFragment: Fragment() , OnMapReadyCallback {
    private lateinit var mMap: GoogleMap
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view: View = inflater.inflate(R.layout.map_fragment, container, false)
        val fragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        fragment?.getMapAsync(this)
        return view
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        mMap.moveCamera(CameraUpdateFactory.zoomTo(12F))
        mMap.moveCamera(CameraUpdateFactory.newLatLng(LatLng(56.015283399999994, 92.8932476)))
        getString(R.string.carwash_coords).split(' ').forEach() {
            val coord = it.split(',')
            mMap.addMarker( MarkerOptions().position(LatLng(coord[0].toDouble(), coord[1].toDouble())).title("Автомойка"))
        }
    }
}