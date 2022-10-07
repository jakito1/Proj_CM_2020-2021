import * as React from 'react';
import { NavigationContainer } from '@react-navigation/native';
import { createStackNavigator } from "@react-navigation/stack";
import CalendarView from './screens/calendarView'
import AgendaView from './screens/agendaView'
import Profile from './screens/profile'
import SignInScreen from './screens/SignInScreen'
import SignUpScreen from './screens/SignUpScreen'

const Stack = createStackNavigator()

const App = () => {
  return (
    <NavigationContainer>
      <Stack.Navigator
        screenOptions={{
          headerShown: false
        }}>
        <Stack.Screen
          name="SignInScreen"
          component={SignInScreen}
        />
        <Stack.Screen
          name="SignUpScreen"
          component={SignUpScreen}
        />
        <Stack.Screen
          name="Calendar"
          component={CalendarView}
        />
        <Stack.Screen
          name="Agenda"
          component={AgendaView}
        />
        <Stack.Screen
          name="Profile"
          component={Profile}
        />
      </Stack.Navigator>
    </NavigationContainer>
  )
}

export default App;