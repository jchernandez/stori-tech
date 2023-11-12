package com.android.rojox.storitech.ui.screens

import android.text.format.DateFormat
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowDownward
import androidx.compose.material.icons.outlined.ArrowUpward
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Lifecycle
import com.android.rojox.storitech.R
import com.android.rojox.storitech.ui.UiApp
import com.android.rojox.storitech.utils.ComposableLifecycle
import com.android.rojox.storitech.utils.amountFormat
import com.android.rojox.storitech.viewmodels.HomeViewModel
import kotlinx.coroutines.launch
import java.util.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment.Companion.Center
import com.android.rojox.core.model.*
import com.android.rojox.storitech.ui.theme.Purple200
import com.android.rojox.storitech.ui.theme.Purple500
import com.android.rojox.storitech.utils.mockDbClient


@OptIn(ExperimentalMaterialApi::class)
@ExperimentalFoundationApi
@Composable
fun HomeScreen(session: UserSession, viewModel: HomeViewModel) {


    val coroutineScope = rememberCoroutineScope()
    val sheetState = rememberModalBottomSheetState(ModalBottomSheetValue.Hidden)

    var movementSelected by remember {
        mutableStateOf(null as AccountMovement?)
    }

    ComposableLifecycle { _, event ->
        if (event == Lifecycle.Event.ON_CREATE) {
            viewModel.getAccountInfo(session.token)
        }
    }

    ModalBottomSheetLayout(
        sheetState = sheetState,
        sheetBackgroundColor = Color.Transparent,
        sheetContent = {
            Box(Modifier.defaultMinSize(minHeight = 1.dp)) {
                if (movementSelected != null) {
                    MovementDetailSheet(movementSelected!!)
                }
            }
        }
    ) {
        HomeScreenContent(
            viewModel = viewModel,
            userName = session.userName
        ) {
            movementSelected = it
            coroutineScope.launch { sheetState.show() }
        }
    }

}

@Composable
private fun HomeScreenContent(
    userName: String,
    viewModel: HomeViewModel,
    onClickMovement:(movement: AccountMovement) -> Unit
) {

    val context = LocalContext.current
    val accountInfo by viewModel.accountData.collectAsState()
    val accountMovements by viewModel.accountMovements.collectAsState()
    val greeting = when (Calendar.getInstance().get(Calendar.HOUR_OF_DAY)) {
        in 0..11 -> context.getString(R.string.good_morning)
        in 12..17 -> context.getString(R.string.good_afternoon)
        else -> context.getString(R.string.good_night)
    }

    UiApp {
        Box(
            Modifier.fillMaxSize()
        ) {
            if (accountInfo?.data != null) {

                val account = accountInfo!!.data!!

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                ) {

                    Column(
                        Modifier
                            .background(Purple500)
                            .padding(16.dp)
                    ) {
                        Text(
                            text = "$greeting, $userName",
                            style = MaterialTheme.typography.h5,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(bottom = 8.dp),
                            color = Color.White
                        )

                        Card (
                            modifier = Modifier.padding(
                                top = 10.dp,
                                bottom = 10.dp
                            ),
                            contentColor = Color.White,
                            backgroundColor = Purple200
                        ) {
                            Column(
                                Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp)
                            ) {
                                Text(
                                    modifier = Modifier
                                        .padding(bottom = 5.dp),
                                    text = stringResource(
                                        id = R.string.current_balance,
                                        account.balance.amountFormat()
                                    ),
                                    style = MaterialTheme.typography.body1,
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight.Bold
                                )

                                Text(
                                    text = "${account.name} - ${account.number}",
                                    style = MaterialTheme.typography.caption,
                                    fontWeight = FontWeight.Bold,
                                )
                            }
                        }
                    }

                    Column(
                        Modifier.padding(16.dp)
                    ) {
                        Text(
                            text = stringResource(id = R.string.last_movements),
                            style = MaterialTheme.typography.subtitle1,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )

                        Box(
                            Modifier.fillMaxWidth(),
                            contentAlignment = Center
                        ) {
                            if (accountMovements?.data != null) {
                                if (!accountMovements!!.data!!.isEmpty()) {
                                    LazyColumn {
                                        items(items =  accountMovements!!.data!!) { item ->
                                            MovementItem(item) {
                                                onClickMovement(item)
                                            }
                                            Divider()
                                        }
                                    }
                                } else {
                                    Text(text = stringResource(id = R.string.no_activity))
                                }
                            } else {
                                CircularProgressIndicator(
                                    modifier = Modifier
                                        .height(100.dp)
                                        .width(100.dp)
                                )
                            }
                        }
                    }


                }

            } else {
                CircularProgressIndicator(
                    modifier = Modifier
                        .height(100.dp)
                        .width(100.dp)
                        .align(Alignment.Center)
                )
            }
        }
    }

}


@Composable
private fun MovementDetailSheet(movement: AccountMovement) {

    Card(
        Modifier
            .height(100.dp)
    ) {

        val icon: ImageVector
        val color: Color

        if (movement.type == MovementType.INCOME) {
            icon = Icons.Outlined.ArrowUpward
            color = Color.Green
        } else {
            icon = Icons.Outlined.ArrowDownward
            color = Color.Red
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            verticalAlignment = CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = CenterVertically,
            ) {
                Icon(
                    imageVector  = icon,
                    icon.name,
                    tint = color,
                )
                Column(
                    modifier = Modifier.padding(
                        start = 10.dp
                    )
                ) {
                    Text(text = movement.description, style = MaterialTheme.typography.body1)
                    Text(
                        modifier = Modifier.padding(
                            top = 5.dp
                        ),
                        text = formatTime(movement.timeStamp),
                        style = MaterialTheme.typography.caption
                    )
                }
            }
            Text(
                text = movement.amount.amountFormat(),
                style = MaterialTheme.typography.caption,
                color = color
            )
        }

    }
}


@Composable
fun MovementItem(movement: AccountMovement, onClick: () -> Unit) {

    val icon: ImageVector
    val color: Color

    if (movement.type == MovementType.INCOME) {
        icon = Icons.Outlined.ArrowUpward
        color = Color.Green
    } else {
        icon = Icons.Outlined.ArrowDownward
        color = Color.Red
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable { onClick() },
        verticalAlignment = CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            verticalAlignment = CenterVertically,
        ) {
            Icon(
                imageVector  = icon,
                icon.name,
                tint = color,
            )
            Column(
                modifier = Modifier.padding(
                    start = 10.dp
                )
            ) {
                Text(text = movement.description, style = MaterialTheme.typography.body1)
                Text(
                    modifier = Modifier.padding(
                        top = 5.dp
                    ),
                    text = formatTime(movement.timeStamp),
                    style = MaterialTheme.typography.caption
                )
            }
        }
        Text(
            text = movement.amount.amountFormat(),
            style = MaterialTheme.typography.caption,
            color = color
        )
    }
}

private fun formatTime(timeStamp: Long): String {
    val date = Date(timeStamp)
    return DateFormat.format("dd MMM yyyy hh:mm", date).toString()

}

@OptIn(ExperimentalFoundationApi::class)
@Composable
@Preview
fun PreviewHomeScreen() {
    val viewModel = HomeViewModel(mockDbClient)
    viewModel.accountData.value = DataState(
        Account("2132313", "Name", "12101", 200.00)
    )
   HomeScreen(
       UserSession(
       "kjjhbjabjsaa",
       "Jose Carlos"),
       viewModel
   )
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
@Preview
fun PreviewMovementItem() {
    MovementItem(
        AccountMovement(
        MovementType.INCOME,
            200.0,
            Date().time,
            "Example")
    ) {

    }
}

