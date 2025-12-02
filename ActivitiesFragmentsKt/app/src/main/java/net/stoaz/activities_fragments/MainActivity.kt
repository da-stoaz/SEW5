package net.stoaz.activities_fragments

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import net.stoaz.activities_fragments.ui.theme.ActivitiesFragmentsTheme

/**
 * The initial Activity (Welcome Screen) provides access to both
 * the creation screen and the overview screen using Intents.
 */
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Enables drawing behind the system bars
        enableEdgeToEdge()
        setContent {
            // Apply the custom theme
            ActivitiesFragmentsTheme {
                // The main layout container
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    // Call the new, enhanced welcome screen Composable
                    WelcomeScreen(
                        modifier = Modifier
                            .padding(innerPadding)
                            .fillMaxSize()
                    )
                }
            }
        }
    }
}

@Composable
fun WelcomeScreen(modifier: Modifier = Modifier) {
    val context = LocalContext.current // Obtain context to start a new activity

    Column(
        modifier = modifier
            .padding(24.dp) // Outer padding for the screen content
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center // Center content vertically
    ) {
        // 1. Title and Message
        Text(
            text = stringResource(R.string.welcome_to_the_item_manager),
            style = MaterialTheme.typography.displaySmall, // Large, impactful title style
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground,
            textAlign = TextAlign.Center
        )

        // Add vertical space
        Spacer(modifier = Modifier.height(16.dp))

        // 2. Subtitle / Message
        Text(
            text = stringResource(R.string.select_an_option_below_to_start_creating_or_view_your_existing_items),
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(horizontal = 16.dp) // Horizontal padding for readability
        )

        // Add vertical space
        Spacer(modifier = Modifier.height(48.dp))

        // 3. Navigation Buttons
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth(0.8f) // Make buttons take up 80% of width
        ) {
            // Button 1: Navigate to Creation Screen
            Button(
                onClick = {
                    val intent = Intent(context, CreateItemActivity::class.java)
                    context.startActivity(intent)
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(Icons.Filled.Add, contentDescription = stringResource(R.string.create_item), Modifier.size(20.dp))
                Spacer(Modifier.width(8.dp))
                Text(stringResource(R.string.create_new_item), style = MaterialTheme.typography.titleMedium)
            }

            // Space between buttons
            Spacer(modifier = Modifier.height(16.dp))

            // Button 2: Navigate to Overview Screen
            OutlinedButton(
                onClick = {
                    val intent = Intent(context, OverviewActivity::class.java)
                    context.startActivity(intent)
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(Icons.Filled.List, contentDescription = stringResource(R.string.view_overview), Modifier.size(20.dp))
                Spacer(Modifier.width(8.dp))
                Text(stringResource(R.string.view_all_items), style = MaterialTheme.typography.titleMedium)
            }
        }
    }
}

@Preview(showBackground = true, device = "spec:width=411dp,height=891dp,dpi=420")
@Composable
fun WelcomeScreenPreview() {
    ActivitiesFragmentsTheme {
        // Use a basic Scaffold structure for the preview to test the screen's appearance
        Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
            WelcomeScreen(
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize()
            )
        }
    }
}
