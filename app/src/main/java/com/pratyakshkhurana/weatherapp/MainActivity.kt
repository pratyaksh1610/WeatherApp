package com.pratyakshkhurana.weatherapp

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.pratyakshkhurana.weatherapp.Adapters.OnSearchViewHistoryItemClicked
import com.pratyakshkhurana.weatherapp.Adapters.SearchViewHistoryAdapter
import com.pratyakshkhurana.weatherapp.Api.RetrofitInstance
import com.pratyakshkhurana.weatherapp.Database.DatabaseClass
import com.pratyakshkhurana.weatherapp.Entity.SearchViewHistory
import com.pratyakshkhurana.weatherapp.Repository.RepositoryClass
import com.pratyakshkhurana.weatherapp.SharedPreferences.SharedPrefs
import com.pratyakshkhurana.weatherapp.ViewModel.ViewModelClass
import com.pratyakshkhurana.weatherapp.ViewModel.ViewModelFactoryClass
import com.pratyakshkhurana.weatherapp.databinding.ActivityMainBinding

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
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.drawerLayout)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // initialise late init variables
        initialiseInstances()

        initialiseSearchViewRecyclerView()

        Log.e("pref", sharedPreferences.getCountryOrCity())
        getCurrentCityWeather(sharedPreferences.getCountryOrCity())

        binding.searchView
            .editText
            .setOnEditorActionListener { v, actionId, event ->
                binding.searchBar.setText(v.text.toString())
                binding.searchView.hide()

                val searchBarText = binding.searchBar.text.toString()

                Log.e("sea", "$searchBarText *")
                if (searchBarText.isNotEmpty()) {
                    searchBarText.trim()
                    searchBarText[0].uppercase()

                    searchBarText.trim()
                    searchBarText[0].uppercase()
                    sharedPreferences.saveCountryOrCity(searchBarText)
                    onShimmerEffect()
                    getCurrentCityWeather(searchBarText)
                    // to get unique entries in search view history recycler view
                    if (viewModel.isPresent(searchBarText) == 0) {
                        viewModel.insertSearchViewHistoryItem(
                            SearchViewHistory(
                                null,
                                searchBarText,
                            ),
                        )
                    }
                    true
                } else {
                    viewModel.getSearchViewHistoryItems().observe(
                        this,
                        Observer {
                            if (it.isEmpty()) {
                                getCurrentCityWeather(sharedPreferences.getCountryOrCity())
                                Log.e("city", sharedPreferences.getCountryOrCity())
                            } else {
                                getCurrentCityWeather(it.last().history)
                                Log.e("city", sharedPreferences.getCountryOrCity())
                            }
                        },
                    )
                    true
                }
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

    private fun getCurrentCityWeather(toString: String) {
        viewModel.getCurrentWeather(toString, RetrofitInstance.API_KEY)
            .observe(
                this@MainActivity,
                Observer { it ->
                    if (it != null) {
                        offShimmerEffect()
                        val cityOrCountry = it.name
                        val code = it.sys.country
                        val concatCodeCityOrCountry = "$cityOrCountry $code"
                        binding.mainWeatherLayout.tvCountryOrCity.text = concatCodeCityOrCountry

                        val desc = it.weather[0].main
                        binding.mainWeatherLayout.weatherDesc.text = desc

                        val humidity = it.main.humidity.toString()
                        binding.mainWeatherLayout.humidityPercentage.text = "$humidity%"

                        binding.mainWeatherLayout.tempCelsius.text = it.main.temp.toString()

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
                        }, 2000)
                    }
                },
            )
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
        getCurrentCityWeather(s.history)
        binding.searchView.editText.setText(s.history)
        binding.searchBar.setText(binding.searchView.text.toString())
        binding.searchView.hide()
    }
}
