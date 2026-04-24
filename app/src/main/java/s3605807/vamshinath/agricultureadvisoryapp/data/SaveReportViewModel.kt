package s3605807.vamshinath.agricultureadvisoryapp.data

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class SaveReportViewModel(application: Application) : AndroidViewModel(application) {

    private val db = DatabaseProvider.getDatabase(application)
    private val reportDao = db.reportDao()

    private val _saveState = MutableStateFlow(false)
    val saveState: StateFlow<Boolean> = _saveState

    private val _reports = MutableStateFlow<List<Report>>(emptyList())
    val reports: StateFlow<List<Report>> = _reports

    fun saveReport(report: Report) {
        viewModelScope.launch {
            reportDao.insertReport(report)
            _saveState.value = true
        }
    }


}

