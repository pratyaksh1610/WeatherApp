package com.pratyakshkhurana.weatherapp

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.pratyakshkhurana.weatherapp.Adapters.OnSearchViewHistoryItemClicked
import com.pratyakshkhurana.weatherapp.Adapters.SearchViewHistoryAdapter
import com.pratyakshkhurana.weatherapp.Adapters.ThisWeekWeatherAdapter
import com.pratyakshkhurana.weatherapp.Api.RetrofitInstance
import com.pratyakshkhurana.weatherapp.DataClass.ThisWeekDataDateAndWeather
import com.pratyakshkhurana.weatherapp.Database.DatabaseClass
import com.pratyakshkhurana.weatherapp.Entity.SearchViewHistory
import com.pratyakshkhurana.weatherapp.Repository.RepositoryClass
import com.pratyakshkhurana.weatherapp.SharedPreferences.SharedPrefs
import com.pratyakshkhurana.weatherapp.ViewModel.ViewModelClass
import com.pratyakshkhurana.weatherapp.ViewModel.ViewModelFactoryClass
import com.pratyakshkhurana.weatherapp.databinding.ActivityMainBinding
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.TimeZone

class MainActivity : AppCompatActivity(), OnSearchViewHistoryItemClicked {
    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: ViewModelClass
    private lateinit var sharedPreferences: SharedPrefs
    private lateinit var adapter: SearchViewHistoryAdapter

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.coordinator)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // initialise late init variables
        initialiseInstances()

        initialiseSearchViewRecyclerView()

        Log.e("pref", sharedPreferences.getCountryOrCity())
        getCurrentCityWeatherData(sharedPreferences.getCountryOrCity())

        binding.searchView
            .editText
            .setOnEditorActionListener { v, actionId, event ->
                binding.searchBar.setText(v.text.toString().lowercase().trim())

                binding.searchView.hide()

                val searchBarText = binding.searchBar.text.toString().lowercase().trim()

                Log.e("searchBarText", "searchBarText : $searchBarText")
                if (searchBarText.isNotEmpty()) {
                    onShimmerEffect()
                    getCurrentCityWeatherData(searchBarText)
                }
                true
            }

        binding.tvClearAll.setOnClickListener {
            viewModel.deleteAllRecyclerViewItems()
            adapter.emptyRecyclerView()
            // last saved item is also delete when clear all is clicked, so update sharedPrefs or not
//            sharedPreferences.saveCountryOrCity("delhi")
        }

        // pull to refresh
        binding.swipeRefreshLayout.setOnRefreshListener {
            getCurrentCityWeatherData(sharedPreferences.getCountryOrCity())
        }
    }

    private fun initialiseInstances() {
        // initialise sharedPrefs object
        sharedPreferences = SharedPrefs(this)

        // use dependency injection (DI) later
        val db = DatabaseClass.getDatabase(this)
        val repo = RepositoryClass(db)
        val factory = ViewModelFactoryClass(repo)
        viewModel = ViewModelProvider(this, factory)[ViewModelClass::class.java]
    }

    private fun onShimmerEffect() {
        binding.shimmer.shimmerLayout.visibility = View.VISIBLE
        binding.shimmer.shimmerLayout.startShimmer()
        binding.mainWeatherLayout.mainWeatherLayoutXml.visibility = View.GONE
    }

    private fun offShimmerEffect() {
        binding.shimmer.shimmerLayout.visibility = View.GONE
        binding.shimmer.shimmerLayout.stopShimmer()
        binding.mainWeatherLayout.mainWeatherLayoutXml.visibility = View.VISIBLE
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun getCurrentCityWeatherData(toString: String) {
        binding.swipeRefreshLayout.isRefreshing = false
        viewModel.getAllWeatherData(toString, RetrofitInstance.API_KEY)

        viewModel.getCurrentWeatherLiveData()
            .observe(
                this@MainActivity,
                Observer { it ->
                    if (it != null) {
                        // to get unique entries in search view history recycler view
//                        sharedPreferences.saveCountryOrCity(toString)
                        if (viewModel.isPresent(toString) == 0) {
                            viewModel.insertSearchViewHistoryItem(
                                SearchViewHistory(null, toString),
                            )
                        }

                        offShimmerEffect()
                        val cityOrCountry = it.name
                        val code = it.sys.country
                        val concatCodeCityOrCountry = "$cityOrCountry $code"
                        binding.mainWeatherLayout.tvCountryOrCity.text = concatCodeCityOrCountry

                        val desc = it.weather[0].main
                        binding.mainWeatherLayout.weatherDesc.text = desc

                        val humidity = it.main.humidity.toString()
                        binding.mainWeatherLayout.humidityPercentage.text = "$humidity%"

                        binding.mainWeatherLayout.tempCelsius.text = it.main.temp.toInt().toString()

                        binding.mainWeatherLayout.windPercent.text =
                            it.wind.speed.toString() + " m/s"

                        binding.mainWeatherLayout.visibilityKm.text =
                            ((it.visibility.toString().toDouble()) / 1000).toString() + " Km"
                    } else {
                        onShimmerEffect()
                        Handler(Looper.getMainLooper()).postDelayed({
                            offShimmerEffect()
                            Toast.makeText(this, "Enter a valid city name", Toast.LENGTH_SHORT)
                                .show()
                            binding.searchBar.setText("")
                        }, 2000)
                    }
                },
            )

        viewModel.getCurrentWeekWeatherLiveData().observe(
            this@MainActivity,
            Observer {
                if (it != null) {
                    binding.mainWeatherLayout.recyclerViewThisWeekWeather.layoutManager =
                        LinearLayoutManager(
                            this,
                            LinearLayoutManager.HORIZONTAL,
                            false,
                        )

                    val listThisWeekDateAndTemperature = mutableListOf<ThisWeekDataDateAndWeather>()
                    for (i in it.list) {
                        val dateAndTime = i.dt_txt
                        val date = dateAndTime.substringBefore(" ").trim()
                        Log.e("pl", i.dt_txt + " - " + date)
                        listThisWeekDateAndTemperature.add(
                            ThisWeekDataDateAndWeather(
                                date,
                                i.main.temp.toInt().toString().trim(),
                                i.weather[0].icon,
                            ),
                        )
                    }
                    val thisWeekTempData = getTemperatureForWeek(listThisWeekDateAndTemperature)

                    val adapter =
                        ThisWeekWeatherAdapter(this@MainActivity, thisWeekTempData)
                    binding.mainWeatherLayout.recyclerViewThisWeekWeather.adapter =
                        adapter
                }
            },
        )
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun getTemperatureForWeek(l: MutableList<ThisWeekDataDateAndWeather>): MutableList<ThisWeekDataDateAndWeather> {
        val uniqueFieldsMap = mutableMapOf<String, Pair<String, String>>()
        for (i in l) {
            i.date = convertDateToDay(i.date)
        }

        // mapping objects
        l.forEach { obj ->
            if (!uniqueFieldsMap.containsKey(obj.date)) {
                uniqueFieldsMap[obj.date] = Pair(obj.temperature, obj.icon)
            }
        }

        Log.e("pll", uniqueFieldsMap.toString())

        val finalThisWeekTempData = mutableListOf<ThisWeekDataDateAndWeather>()

        uniqueFieldsMap.forEach { (firstField, pair) ->
            val (secondField, thirdField) = pair
            finalThisWeekTempData.add(
                ThisWeekDataDateAndWeather(
                    firstField,
                    secondField,
                    thirdField,
                ),
            )
        }

        return finalThisWeekTempData
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun convertDateToDay(dateString: String): String {
        // Parse the date string into a LocalDate object
        val date = LocalDate.parse(dateString)

        // Get the day of the week corresponding to the date
        val dayOfWeek = date.dayOfWeek

        // Convert DayOfWeek enum to the name of the day
        var weekDayName =
            dayOfWeek.toString().lowercase()
                .replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }

        println("The day corresponding to $dateString is $weekDayName.")
        when (weekDayName) {
            "Monday" -> weekDayName = "Mon"
            "Tuesday" -> weekDayName = "Tue"
            "Wednesday" -> weekDayName = "Wed"
            "Thursday" -> weekDayName = "Thu"
            "Friday" -> weekDayName = "Fri"
            "Saturday" -> weekDayName = "Sat"
            "Sunday" -> weekDayName = "Sun"
        }
        return weekDayName
    }

    private fun getTimeInAmPm(input: String): String {
        val inputFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        val outputFormat = SimpleDateFormat("hh a", Locale.getDefault())

        val date = inputFormat.parse(input)
        val outputTimeString = outputFormat.format(date!!)
        Log.e("date", "getCurrentWeatherEveryThreeHours: $outputTimeString")
        return outputTimeString
    }

    private fun todaysDate(): String {
        // Get current date
        // Get current date
        val calendar: Calendar = Calendar.getInstance()
        val dateFormat = SimpleDateFormat("yyyy-MM-dd")
        val currentDate: String = dateFormat.format(calendar.time)

        Log.d("CurrentDate", currentDate)
        return currentDate
    }

    private fun convertEpochTimeStampDtxToIst() {
        // Convert Epoch Unix Timestamp to GMT
        val currentEpochTimestamp = System.currentTimeMillis() / 1000

        val date = Date(currentEpochTimestamp * 1000L)
        val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        sdf.timeZone = TimeZone.getTimeZone("Asia/Kolkata")
        val gmtTime = sdf.format(date)

        // Print or use the GMT time as needed
        Log.d("GMT", gmtTime)
    }

    private fun initialiseSearchViewRecyclerView() {
        viewModel.getSearchViewHistoryItems().observe(
            this@MainActivity,
            Observer {
                if (it.isEmpty()) {
                    Log.e("empty", "no history found")
                } else {
                    val listOfSearchViewHistoryItems: MutableList<SearchViewHistory> =
                        it as MutableList<SearchViewHistory>
                    binding.recyclerViewSearchItems.layoutManager =
                        LinearLayoutManager(this@MainActivity)
                    adapter =
                        SearchViewHistoryAdapter(
                            listOfSearchViewHistoryItems,
                            this@MainActivity,
                        )
                    binding.recyclerViewSearchItems.adapter = adapter
                }
            },
        )
    }

    // delete search history item when click on close icon
    override fun deleteItem(s: SearchViewHistory) {
        viewModel.deleteSearchViewHistoryItem(s)
    }

    // get weather when click on any search history item text
    override fun getWeather(s: SearchViewHistory) {
        getCurrentCityWeatherData(s.history)
        binding.searchView.editText.setText(s.history)
        binding.searchBar.setText(binding.searchView.text.toString())
        binding.searchView.hide()
    }
}
