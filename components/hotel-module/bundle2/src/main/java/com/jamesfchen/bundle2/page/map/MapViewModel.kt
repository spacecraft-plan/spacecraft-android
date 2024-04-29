package com.jamesfchen.bundle2.page.map

import android.app.Application
import android.location.Geocoder
import androidx.lifecycle.AndroidViewModel
import androidx.paging.Config
import androidx.paging.toLiveData
import com.jamesfchen.bundle2.db.LbsDb
import com.jamesfchen.bundle2.db.model.LBS
import com.jamesfchen.bundle2.util.ioThread
import com.jamesfchen.bundle2.util.reverseGeocode2String
import java.util.*

class MapViewModel(app: Application) : AndroidViewModel(app) {
    val dao = LbsDb.get(app).lbsDao()
    val allLbsDatas = dao.allLbsDatas().toLiveData(Config(
            pageSize = 30,
            enablePlaceholders = true,
            maxSize = 200
    ))
    val needUploadDatas = dao.getNeedUploadDatas().toLiveData(Config(
            pageSize = 200,
            enablePlaceholders = false
    ))
    var geocoder: Geocoder = Geocoder(app.applicationContext, Locale.CHINESE)
    fun insert(needUpload: Boolean = false, appCellInfo: _root_ide_package_.com.jamesfchen.bundle2.model.AppCellInfo?, appLocation: _root_ide_package_.com.jamesfchen.bundle2.model.AppLocation?) = ioThread {
        val lbs = LBS()
        lbs.appCellInfo = appCellInfo
        lbs.appLocation = appLocation
        lbs.needUpload = needUpload
        lbs.add = geocoder.reverseGeocode2String(appLocation?.lat?:0.0,appLocation?.lon?:0.0)
        dao.insert(lbs)
    }
    fun update(lbs: LBS) = ioThread {
        dao.update(lbs)
    }
    fun delete(pos:Int){
        val lbs = allLbsDatas.value?.get(pos)
        lbs?.let { delete(it) }
    }
    fun delete(lbs: LBS) = ioThread {
        dao.delete(lbs)
    }
    fun clearMockData() = ioThread {
        val toList = dao.getAll()
        for (lbs in toList) {
            val isMockData=lbs.appCellInfo?.isMockData ==true || lbs.appLocation?.isMockData ==true
            if (isMockData) dao.delete(lbs)
        }
    }
    fun clearAll() = ioThread {
        val toList = dao.getAll()
        for (lbs in toList) {
            dao.delete(lbs)
        }
    }
}