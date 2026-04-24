package s3605807.vamshinath.agricultureadvisoryapp.data

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ReportListViewModel(application: Application) : AndroidViewModel(application) {

    private val reportDao = DatabaseProvider.getDatabase(application).reportDao()

    private val _reports = MutableStateFlow<List<Report>>(emptyList())
    val reports: StateFlow<List<Report>> = _reports

    init {
        loadReports()
    }

    fun loadReports() {
        viewModelScope.launch {
            _reports.value = reportDao.getAllReports()
        }
    }



    fun deleteReport(report: Report) {
        viewModelScope.launch {
            reportDao.deleteReport(report)
            loadReports() // refresh list
        }
    }
}
