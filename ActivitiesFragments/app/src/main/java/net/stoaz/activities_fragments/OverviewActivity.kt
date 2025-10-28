package net.stoaz.activities_fragments

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import net.stoaz.activities_fragments.ui.theme.ActivitiesFragmentsTheme

// --- Data Structure for Homework Scope ---

// Keys used for passing data via Intent
const val EXTRA_ITEM_NAME = "extra_item_name"
const val EXTRA_ITEM_DESCRIPTION = "extra_item_description"

data class ItemData(
    val name: String,
    val description: String = "No description provided."
)

/**
 * Simple static data store to hold the list of items for the duration of the app's process.
 * This simulates a database for homework purposes.
 */
object AppItems {
    val list = mutableListOf(
        ItemData("Homework Planner", "Check due dates for all classes."),
        ItemData("Grocery List", "Milk, Eggs, Bread."),
        ItemData("Fitness Goal", "Run 5K by end of the month.")
    )
}

// --- End Data Structure ---


/**
 * Activity for the "Overview" screen.
 * This is launched by an Intent from MainActivity or CreateItemActivity.
 */
class OverviewActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // 1. CHECK INCOMING INTENT FOR NEW ITEM DATA
        // If the Intent contains the required key, a new item was saved from CreateItemActivity.
        if (intent.hasExtra(EXTRA_ITEM_NAME)) {
            val newItemName = intent.getStringExtra(EXTRA_ITEM_NAME) ?: "Untitled Item"
            val newItemDescription = intent.getStringExtra(EXTRA_ITEM_DESCRIPTION) ?: "No description provided."

            // Add the new item to our static list
            AppItems.list.add(ItemData(newItemName, newItemDescription))
        }

        setContent {
            ActivitiesFragmentsTheme {
                OverviewScreen(
                    modifier = Modifier.fillMaxSize()
                )
            }
        }
    }
}

/**
 * Composable for the Item Overview screen.
 * Includes a stable custom header with a back button.
 */
@Composable
fun OverviewScreen(modifier: Modifier = Modifier) {
    val context = LocalContext.current

    // Read the current list from the static data store
    val itemsList = AppItems.list.toList()

    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        // --- Stable Custom Header with Back Button (FIXED INSETS) ---
        Surface(
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier
                .fillMaxWidth()
                // FIX: Apply top safe drawing insets to push content below the status bar/notch.
                .windowInsetsPadding(WindowInsets.safeDrawing.only(WindowInsetsSides.Top)),
            tonalElevation = 4.dp
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Back Button: Uses the standard finish() call to close this Activity
                IconButton(
                    onClick = { (context as ComponentActivity).finish() },
                ) {
                    Icon(
                        imageVector = Icons.Filled.ArrowBack,
                        contentDescription = "Back to Home",
                        tint = MaterialTheme.colorScheme.onPrimary
                    )
                }

                Text(
                    text = "Items Overview",
                    style = MaterialTheme.typography.headlineMedium,
                    color = MaterialTheme.colorScheme.onPrimary,
                    modifier = Modifier.padding(start = 8.dp)
                )
            }
        }
        // -----------------------------

        // Main content area
        Column(
            modifier = Modifier
                .padding(horizontal = 24.dp, vertical = 16.dp)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // Top section: The list of items
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = "Currently Created Items (${itemsList.size}):",
                    style = MaterialTheme.typography.headlineSmall,
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                // Displaying the list of items
                LazyColumn(modifier = Modifier.fillMaxWidth()) {
                    items(itemsList) { item ->
                        ListItem(
                            headlineContent = { Text(item.name) },
                            supportingContent = { Text(item.description) },
                            modifier = Modifier.fillMaxWidth()
                        )
                        HorizontalDivider(
                            Modifier,
                            DividerDefaults.Thickness,
                            DividerDefaults.color
                        )
                    }
                }
            }

            // Bottom section: Navigation button back to CreateItemActivity
            OutlinedButton(
                onClick = {
                    // Intent to navigate to the CreateItemActivity
                    val intent = Intent(context, CreateItemActivity::class.java)
                    context.startActivity(intent)
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(Icons.Filled.Add, contentDescription = "Create Item", Modifier.size(20.dp))
                Spacer(Modifier.width(8.dp))
                Text("Go to Creation Screen")
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun OverviewPreview() {
    ActivitiesFragmentsTheme {
        Box(modifier = Modifier.fillMaxSize()) {
            OverviewScreen()
        }
    }
}
