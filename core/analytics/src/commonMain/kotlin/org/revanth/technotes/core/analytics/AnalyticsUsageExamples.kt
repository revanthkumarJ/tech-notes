/*
 * Copyright 2025 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See See https://github.com/openMF/kmp-project-template/blob/main/LICENSE
 */
@file:Suppress("ktlint:standard:discouraged-comment-location", "ModifierMissing", "SpreadOperator")

package org.revanth.technotes.core.analytics

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import template.core.base.analytics.AnalyticsEvent
import template.core.base.analytics.AnalyticsHelper
import template.core.base.analytics.AnalyticsValidator
import template.core.base.analytics.MockAnalyticsHelper
import template.core.base.analytics.NoOpAnalyticsHelper
import template.core.base.analytics.Param
import template.core.base.analytics.assertEventCount
import template.core.base.analytics.assertEventLogged
import template.core.base.analytics.assertUserProperty
import template.core.base.analytics.batch
import template.core.base.analytics.createTestAnalyticsHelper
import template.core.base.analytics.lifecycleTracker
import template.core.base.analytics.rememberAnalyticsHelper
import template.core.base.analytics.sanitize
import template.core.base.analytics.validate
import template.core.base.analytics.withValidation
import kotlin.time.Clock

/**
 * Example usage of the enhanced Mifos analytics functionality. This file
 * demonstrates various ways to use the analytics features.
 */

/** Example: Basic analytics usage */
class BasicAnalyticsExample(private val analytics: AnalyticsHelper) {

    fun demonstrateBasicUsage() {
        // Simple event logging
        analytics.logEvent("user_action", "action" to "click", "element" to "save_button")

        // Using convenience methods
        analytics.logScreenView("ClientList")
        analytics.logButtonClick("create_client", "ClientList")
        analytics.logError("Network timeout", "NET_001", "ClientDetails")
        analytics.logFeatureUsed("biometric_auth", "LoginScreen")

        // Using builder pattern
        val event = AnalyticsEvent("form_submitted", emptyList())
            .withParam("form_name", "client_registration")
            .withParam("field_count", "12")
            .withParam("completion_time", "45s")
        analytics.logEvent(event)

        // Batch logging for performance
        analytics.batch()
            .add("client_created", "client_id" to "C001")
            .add("document_uploaded", "doc_type" to "photo")
            .add("form_completed", "form_name" to "registration")
            .flush()
    }
}

/** Example: Mifos-specific analytics usage */
class TechNotesAnalyticsExample(private val analytics: AnalyticsHelper) {

    private val technotesTracker = analytics.technotesTracker()

    fun demonstrateTechNotesFeatures() {
        // Client operations
        technotesTracker.trackClientOperation(
            operation = "create",
            clientId = "C12345",
            success = true,
            duration = 1500L,
        )

        // Loan operations
        technotesTracker.trackLoanOperation(
            operation = "apply",
            loanType = "personal",
            amount = "50000",
            success = true,
        )

        // Savings operations
        technotesTracker.trackSavingsOperation(
            operation = "deposit",
            accountId = "S67890",
            amount = "10000",
            success = true,
        )

        // Performance tracking
        technotesTracker.trackPerformance(
            operation = "sync_clients",
            duration = 2500L,
            success = true,
            additionalMetrics = mapOf(
                "record_count" to "150",
                "network_type" to "wifi",
            ),
        )

        // Using extensions
        analytics.trackClientCreationFlow("personal_info", success = true)
        analytics.trackApiCall("/api/clients", "GET", 250L, 200)
        analytics.trackNavigation("ClientList", "ClientDetails", "item_click")
        analytics.trackValidationError("ClientForm", "phoneNumber", "invalid_format")
    }
}

/** Example: Compose integration */
@Composable
fun ClientDetailsScreen(
    clientId: String,
    onNavigateBack: () -> Unit,
) {
    // Automatic screen tracking with business context
    TrackTechNotesScreen(
        screenName = "ClientDetails",
        clientId = clientId,
        additionalParams = mapOf("source" to "client_list"),
    )

    // Track flow progress
    TrackTechNotesFlowCompletion(
        flowName = "client_onboarding",
        step = "3",
        totalSteps = 5,
        entityId = clientId,
    )

    // Remember analytics helper
    val analytics = rememberAnalyticsHelper()
    val documentTracker = rememberTechNotesDocumentTracker()
    val technotesAnalytics = rememberTechNotesAnalyticsTracker()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
    ) {
        // Button with automatic click tracking
        Button(
            modifier = Modifier.trackClientAction("view_loans", clientId),
            onClick = {
                analytics.logEvent(
                    "client_action",
                    TechNotesParamKeys.CLIENT_ID to clientId,
                    "action" to "view_loans",
                )
            },
        ) {
            Text("View Loans")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                documentTracker.trackUpload("identity_proof", 1024 * 1024)
            },
        ) {
            Text("Upload Document")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                technotesAnalytics.trackClientOperation("update", clientId, success = true)
                onNavigateBack()
            },
        ) {
            Text("Save Changes")
        }
    }

    // Track form field interactions
    TrackTechNotesFormField(
        fieldName = "client_name",
        formName = "client_details",
        fieldType = "text",
    )
}

/** Example: Survey tracking */
@Composable
fun SurveyScreen(surveyId: String) {
    val analytics = rememberTechNotesAnalyticsTracker()
    var currentQuestion by remember { mutableStateOf(1) }
    val totalQuestions = 5

    // Track survey start
    TrackTechNotesSurvey(surveyId, action = "started")

    Column {
        Text("Question $currentQuestion of $totalQuestions")

        Button(
            onClick = {
                // Track question answered
                analytics.trackSurveyOperation(
                    operation = "answered",
                    surveyId = surveyId,
                    questionCount = currentQuestion,
                    completionTime = 1500L,
                )

                if (currentQuestion < totalQuestions) {
                    currentQuestion++
                } else {
                    // Track survey completion
                    analytics.trackSurveyOperation(
                        operation = "completed",
                        surveyId = surveyId,
                    )
                }
            },
        ) {
            Text(if (currentQuestion < totalQuestions) "Next" else "Complete")
        }
    }
}

/** Example: Testing analytics */
class AnalyticsTestingExample {

    fun demonstrateTestingUtilities() {
        // Create test analytics helper
        val testAnalytics = createTestAnalyticsHelper()

        // Use analytics in your code
        testAnalytics.logScreenView("TestScreen")
        testAnalytics.logButtonClick("test_button", "TestScreen")
        testAnalytics.setUserId("test_user_123")

        // Verify events were logged
        testAnalytics.assertEventLogged("screen_view", mapOf("screen_name" to "TestScreen"))
        testAnalytics.assertEventCount("button_click", 1)
        testAnalytics.assertUserProperty("user_id", "test_user_123")

        // Check specific events
        // assert(testAnalytics.hasScreenView("TestScreen"))
        // assert(testAnalytics.hasButtonClick("test_button"))

        // Print events for debugging
        testAnalytics.printEvents()

        // Use mock analytics with network simulation
        val mockAnalytics = MockAnalyticsHelper(
            simulateFailures = true,
            failureRate = 0.1f,
        )

        // Test with mock
        mockAnalytics.logEvent("test_event", "param" to "value")
        // assert(mockAnalytics.getEventCount("test_event") <= 1)
    }
}

/** Example: Data validation */
object AnalyticsValidationExample {

    fun demonstrateValidation() {
        val validator = AnalyticsValidator()

        // Validate an event
        val event = AnalyticsEvent(
            "user-action",
            listOf(Param("very_long_key_that_exceeds_limit", "value")),
        )
        val validationResult = event.validate(validator)

        if (!validationResult.isValid) {
            println("Validation errors: ${validationResult.errorMessages}")
        }

        // Sanitize invalid event
        val sanitizedEvent = event.sanitize(validator)
        println("Sanitized event: $sanitizedEvent")

        // Use validating analytics helper
        val originalAnalytics = NoOpAnalyticsHelper()
        val validatingAnalytics = originalAnalytics.withValidation(
            strictMode = false, // Sanitize instead of throwing
            logValidationErrors = true,
        )

        // This will be sanitized automatically
        validatingAnalytics.logEvent(event)
    }
}

/** Example: App lifecycle tracking */
class AppLifecycleExample(analytics: AnalyticsHelper) {

    private val lifecycleTracker = analytics.lifecycleTracker()

    fun onAppLaunch() {
        lifecycleTracker.markAppLaunchStart()
        // ... app initialization
        lifecycleTracker.markAppLaunchComplete()
    }

    fun onAppBackground() {
        lifecycleTracker.markAppBackground()
    }

    fun onAppForeground() {
        lifecycleTracker.markAppForeground()
    }
}

/** Example: Report tracking */
@Composable
fun ReportScreen() {
    val reportTracker = rememberTechNotesReportTracker()
    val analytics = rememberAnalyticsHelper()

    Column {
        Button(
            onClick = {
                val startTime = Clock.System.now().toEpochMilliseconds()
                // Generate report...
                val duration = Clock.System.now().toEpochMilliseconds() - startTime

                reportTracker.trackGeneration(
                    reportName = "client_summary",
                    filters = mapOf("office" to "main_branch", "date_range" to "last_month"),
                    duration = duration,
                    success = true,
                )
            },
        ) {
            Text("Generate Report")
        }

        Button(
            onClick = {
                reportTracker.trackExport("client_summary", "pdf", success = true)
            },
        ) {
            Text("Export as PDF")
        }

        Button(
            onClick = {
                reportTracker.trackShare("client_summary", "email")
            },
        ) {
            Text("Share Report")
        }
    }
}

/** Usage in DI setup */
object AnalyticsDIExample {
    /*
    fun setupAnalyticsDI() = module {
        // Basic analytics
        single<AnalyticsHelper> { FirebaseAnalyticsHelper(get()) }

        // With validation
        single<AnalyticsHelper>("validated") {
            get<AnalyticsHelper>().withValidation(strictMode = false)
        }

        // Mifos tracker
        single<TechNotesAnalyticsTracker> { TechNotesAnalyticsTracker(get()) }

        // Performance tracker
        single<PerformanceTracker> { get<AnalyticsHelper>().performanceTracker() }

        // Memory tracker
        single<MemoryTracker> { get<AnalyticsHelper>().memoryTracker() }
    }
     */
}
