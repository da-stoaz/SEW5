package net.stoaz.activities_fragments

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.List
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import net.stoaz.activities_fragments.data.ItemData
import net.stoaz.activities_fragments.ui.theme.ActivitiesFragmentsTheme


const val EXTRA_NEW_ITEM = "extra_new_item"


/**
 * Activity for the "Create" screen.
 * This is launched by an Intent from MainActivity.
 */
class CreateItemActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ActivitiesFragmentsTheme {
                CreateItemScreen(
                    modifier = Modifier.fillMaxSize()
                )
            }
        }
    }
}

/**
 * Composable for the Item Creation screen with an input field and navigation buttons.
 */
@Composable
fun CreateItemScreen(modifier: Modifier = Modifier) {
    val context = LocalContext.current
    var itemName by remember { mutableStateOf("") }
    var itemDescription by remember { mutableStateOf("") }

    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        // --- Stable Custom Header with Back Button ---
        Surface(
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier
                .fillMaxWidth()
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
                        contentDescription = stringResource(R.string.back_to_home),
                        tint = MaterialTheme.colorScheme.onPrimary
                    )
                }

                Text(
                    text = stringResource(R.string.create_new_item),
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
            // Top section: Input fields and Save Button
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = stringResource(R.string.enter_details_for_your_new_item),
                    style = MaterialTheme.typography.headlineSmall,
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                // Input for Item Name
                OutlinedTextField(
                    value = itemName,
                    onValueChange = { itemName = it },
                    label = { Text(stringResource(R.string.item_name_required)) },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Input for Item Description
                OutlinedTextField(
                    value = itemDescription,
                    onValueChange = { itemDescription = it },
                    label = { Text(stringResource(R.string.description)) },
                    singleLine = false,
                    maxLines = 4,
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(min = 100.dp)
                )

                Spacer(modifier = Modifier.height(32.dp))

                // Button to "Create" the item (UPDATED LOGIC)
                Button(
                    onClick = {
                        // 1. Create the Intent to navigate to the OverviewActivity
                        val intent = Intent(context, OverviewActivity::class.java)

                        // 2. Use Intent.putExtra() to attach the data
                        val itemData = if (itemDescription.isBlank()) {
                            ItemData(itemName)
                        } else {
                            ItemData(itemName, itemDescription)
                        }
                        intent.putExtra(EXTRA_NEW_ITEM, itemData)


                        // 3. Start the Activity
                        context.startActivity(intent)

                        // 4. Close the current (Create) Activity since the form submission is complete
                        (context as ComponentActivity).finish()
                    },
                    enabled = itemName.isNotBlank(), // Only enabled if a name is provided
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(stringResource(R.string.save_and_create))
                }
            }

            // Bottom section: Navigation button to OverviewActivity
            OutlinedButton(
                onClick = {
                    // Intent to navigate to the OverviewActivity
                    val intent = Intent(context, OverviewActivity::class.java)
                    context.startActivity(intent)
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(Icons.Filled.List, contentDescription = stringResource(R.string.view_list), Modifier.size(20.dp))
                Spacer(Modifier.width(8.dp))
                Text(stringResource(R.string.view_all_created_items))
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CreateItemPreview() {
    ActivitiesFragmentsTheme {
        // Use a basic container to display the preview correctly
        Box(modifier = Modifier.fillMaxSize()) {
            CreateItemScreen()
        }
    }
}
