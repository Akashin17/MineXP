package com.example.minexp.ui.screen.homepage

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.minexp.ui.screen.navigation.BottomNavigationBar

@Composable
fun HomeScreenContent () {
    val todoItems = remember { mutableStateListOf("Valorant", "Belajar Compose", "Olahraga", "Meditasi") }
    val checkedStates = remember { mutableStateListOf(false, false, false, false) }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF8F8F8))
            .padding(16.dp)
            .padding(16.dp)
    ) {
        HeaderSection()
        Spacer(modifier = Modifier.height(16.dp))
        XPLevelCard()
        Spacer(modifier = Modifier.height(16.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            TodoList(todoItems, checkedStates)
            RecentActivityCard()
        }
    }
}

@Composable
fun HeaderSection() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text("Hello, People!", fontWeight = FontWeight.Bold, fontSize = 20.sp)
        Icon(
            imageVector = Icons.Default.Notifications,
            contentDescription = "Notifikasi",
            modifier = Modifier
                .background(Color.LightGray, shape = CircleShape)
                .padding(8.dp)
        )
    }
}

@Composable
fun XPLevelCard() {
    Card(
        shape = RoundedCornerShape(30.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF8A64D6)),
        modifier = Modifier
            .fillMaxWidth()
            .height(150.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxSize()
        ) {
            Text("Level 1", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 22.sp)
            Text("49/50 XP to level 2", color = Color.White, fontSize = 14.sp)
            Spacer(modifier = Modifier.height(10.dp))
            LinearProgressIndicator(
                progress = 0.98f,
                modifier = Modifier
                    .fillMaxWidth(0.8f)
                    .height(6.dp),
                color = Color(0xFFB3E5FC),
                trackColor = Color.White.copy(alpha = 0.3f)
            )
        }
    }
}

@Composable
fun TodoList(items: List<String>, checkedStates: MutableList<Boolean>) {
    Card(
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        modifier = Modifier
            .width(150.dp)
            .height(250.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("TODO :", fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(8.dp))
            items.forEachIndexed { index, item ->
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Checkbox(
                        checked = checkedStates[index],
                        onCheckedChange = { checkedStates[index] = it }
                    )
                    Text(item, fontSize = 12.sp)
                }
            }
        }
    }
}

@Composable
fun RecentActivityCard() {
    Card(
        shape = RoundedCornerShape(30.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        modifier = Modifier
            .width(150.dp)
            .height(250.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Placeholder Gambar
            Box(
                modifier = Modifier
                    .size(100.dp)
                    .background(Color.LightGray, shape = RoundedCornerShape(20.dp))
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text("Recent", fontWeight = FontWeight.Bold, fontSize = 18.sp)
            Spacer(modifier = Modifier.height(4.dp))
            Text("Minecraft", fontWeight = FontWeight.SemiBold)
            Text(
                "Lorem ipsum dolor sit amet. first we mine, then we craft. lests mine...",
                fontSize = 12.sp
            )
        }
    }
}

