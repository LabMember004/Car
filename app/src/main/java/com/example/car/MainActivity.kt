package com.example.car

import CarDetailsPage
import android.graphics.Paint
import android.os.Bundle
import android.widget.Button
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.IconButton
import androidx.compose.material.TextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Card
import androidx.compose.material3.Divider
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.car.ui.theme.CarTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CarTheme {
                var selectedIndex by remember { mutableStateOf(0) }
                var selectedCar by remember { mutableStateOf<Car?>(null) }
                var searchQuery by remember { mutableStateOf("") }
                var isSearchFieldVisible by remember { mutableStateOf(false) }


                if (selectedCar == null) {
                    CarList(onCarClick = { car -> selectedCar = car }, searchQuery = searchQuery)
                } else {
                    CarDetailsPage(car = selectedCar!!)
                }

                Scaffold(
                    topBar = { Headers(
                        showBackButton = selectedCar !=null,
                        onBackClicked = {selectedCar = null},
                        searchQuery = searchQuery,
                        onSearchQueryChanged = {newQuery -> searchQuery = newQuery}
                    ) },
                    bottomBar = {
                        NavBar(
                            selectedIndex = selectedIndex,
                            onItemSelected = { index ->

                                selectedIndex = index
                            }
                        )
                    }
                ) { paddingValues ->
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(paddingValues)
                    ) {
                        when (selectedIndex) {
                            0 -> {
                                if (selectedCar == null) {

                                    CarList(onCarClick = { car -> selectedCar = car } , searchQuery = searchQuery)
                                } else {
                                    CarDetailsPage(car = selectedCar!!)
                                }
                            }
                            1 -> {
                                Text(
                                    text = "Favorites",
                                    modifier = Modifier.fillMaxSize(),
                                    textAlign = TextAlign.Center
                                )
                            }
                            2 -> {
                                Text(
                                    text = "Settings",
                                    modifier = Modifier.fillMaxSize(),
                                    textAlign = TextAlign.Center
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
@Composable
fun Headers(
    showBackButton: Boolean,
    onBackClicked: () -> Unit,
    searchQuery: String,
    onSearchQueryChanged: (String) -> Unit
) {
    var isSearchVisible by remember { mutableStateOf(false) }
    val focusManager = LocalFocusManager.current
    val focusRequester = remember { FocusRequester() }

    LaunchedEffect(isSearchVisible) {
        if (isSearchVisible) {
            focusRequester.requestFocus()
        } else {
            focusManager.clearFocus()
        }
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 17.dp, vertical = 30.dp)
            .animateContentSize(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (showBackButton) {
            IconButton(onClick = onBackClicked) {
                Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Back")
            }
        }

        if (!isSearchVisible) {
            Text(
                text = "Available cars",
                fontSize = 22.sp,
                modifier = Modifier.weight(1f),
                textAlign = TextAlign.Center
            )
        }

        IconButton(onClick = {
            isSearchVisible = !isSearchVisible
        }) {
            Icon(imageVector = Icons.Default.Search, contentDescription = "Search")
        }

        if (isSearchVisible) {
            TextField(
                value = searchQuery,
                onValueChange = onSearchQueryChanged,
                placeholder = { Text("Search cars...") },
                modifier = Modifier
                    .fillMaxWidth(0.5f)
                    .focusRequester(focusRequester),
                keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
                keyboardActions = KeyboardActions(
                    onDone = { focusManager.clearFocus() }
                )
            )
        }

        if (isSearchVisible) {
            IconButton(onClick = {
                isSearchVisible = false
                onSearchQueryChanged("")
            }) {
                Icon(imageVector = Icons.Default.Close, contentDescription = "Close")
            }
        }
    }
}






@Composable
fun Cars(car: Car, onCarClick: (Car) -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp)
            .wrapContentSize(Alignment.Center)
            .clickable { onCarClick(car) }
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .height(175.dp)
        ) {
            Row(
                modifier = Modifier,
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(id = car.imageId),
                    contentDescription = "",
                    modifier = Modifier
                        .fillMaxWidth(0.5f)
                        .height(140.dp)
                )
                Column(modifier = Modifier.fillMaxWidth()) {
                    Text(
                        text = car.title,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Row(
                        modifier = Modifier
                            .fillMaxWidth(0.9f)
                            .padding(vertical = 8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(car.brand)
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.End
                        ) {
                            Icon(imageVector = Icons.Default.Star, contentDescription = null)
                            Text(
                                text = car.rating,
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }
            }

            HorizontalDivider(
                modifier = Modifier
                    .fillMaxWidth(0.9f)
                    .padding(horizontal = 8.dp)
                    .align(Alignment.CenterHorizontally),
                thickness = 1.dp,
                color = Color.Gray
            )
            Text(
                text = car.price,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth(),
                fontSize = 20.sp,
                letterSpacing = 3.sp
            )
        }
    }
}
@Composable
fun CarList(onCarClick: (Car) -> Unit, searchQuery: String) {
    var showHighestRating by remember { mutableStateOf(false) }
    var showAffordableCars by remember { mutableStateOf(false) }
    val cars = listOf(
        Car(R.drawable.jeep04, "Jeep Rubicon", "Jeep", "5.0", "20,000$"),
        Car(R.drawable.nissan01, "Nissan Altima", "Nissan", "4.8", "15,000$"),
        Car(R.drawable.toyota06, "Toyota Corolla", "Toyota", "3.7", "12,000$"),
        Car(R.drawable.ford04, "Ford Fusion", "Ford", "5.0", "18,000$"),
        Car(R.drawable.kia01, "Kia Stinger", "Kia", "4.2", "27,000$")
    )

    // Step 1: Apply search filter
    val filteredCars = cars.filter { car ->
        car.title.contains(searchQuery, ignoreCase = true)
    }

    // Step 2: Apply rating and price filters
    val finalFilteredCars = filteredCars.filter { car ->
        (!showHighestRating || car.rating.toFloat() >= 4.5f) &&  // High Rating: rating >= 4.5
                (!showAffordableCars || car.price.replace(Regex("[^0-9]"), "").toFloat() < 15000)  // Affordable Cars: price < 15000
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState())
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            OutlinedButton(
                onClick = { showHighestRating = !showHighestRating }
            ) {
                Text(if (showHighestRating) "High Rating: ON" else "High Rating: OFF")
            }
            OutlinedButton(
                onClick = { showAffordableCars = !showAffordableCars }
            ) {
                Text(if (showAffordableCars) "Affordable Cars: ON" else "Affordable Cars: OFF")
            }
        }
    }

    // Step 3: Display the final filtered list
    finalFilteredCars.forEach { car ->
        Cars(car = car, onCarClick = onCarClick)
    }
}



@Composable
fun NavBar(
    selectedIndex: Int,
    onItemSelected: (Int) -> Unit
) {
    val navItemList = listOf(
        NavItem("Home", Icons.Default.Home),
        NavItem("Favorites", Icons.Default.Favorite),
        NavItem("Settings", Icons.Default.Settings)
    )
    NavigationBar {
        navItemList.forEachIndexed { index, navItem ->
            NavigationBarItem(
                selected = selectedIndex == index,
                onClick = { onItemSelected(index) },
                icon = { Icon(imageVector = navItem.icon, contentDescription = navItem.Label) },
                label = { Text(text = navItem.Label) }
            )
        }
    }
}

