import * as React from 'react';
import { Text, View, StyleSheet, TouchableOpacity } from 'react-native';
import auth from '@react-native-firebase/auth';
import LinearGradient from 'react-native-linear-gradient';



const Profile = ({ navigation }) => {

  const signOut = async () => {
    try {
      await auth().signOut().then(() => { navigation.navigate('SignInScreen') })
        .catch(error => {
          console.log('Something went wrong with sign in: ', error);
        });
    } catch (e) {
      console.log(e);
    }
  }

  return (
    <View style={styles.view}>
      <View style={styles.view}>
        <Text style={styles.title}>Profile</Text>
        <Text style={styles.text}>Current Email:</Text>
        <Text>{auth().currentUser.email}{"\n"}</Text>
        <TouchableOpacity style={styles.grade} onPress={() => navigation.navigate('Calendar')}>
          <LinearGradient colors={['#08d4c4', '#01ab9d']} style={styles.grade}>
            <Text style={styles.button}> Home </Text>
          </LinearGradient>
        </TouchableOpacity>
        <TouchableOpacity style={styles.grade} onPress={() => signOut()}>
          <LinearGradient colors={['#08d4c4', '#01ab9d']} style={styles.grade}>
            <Text style={styles.button}> Log out </Text>
          </LinearGradient>
        </TouchableOpacity>
      </View>
    </View>
  )
}

const styles = StyleSheet.create({
  text: {
    fontSize: 16,
    textAlign: "center",
    fontWeight: 'bold'
  },
  button: {
    fontSize: 16,
    color: "white",
    padding: 10,
    borderRadius: 10,
    width: 200,
    textAlign: "center",
    fontWeight: 'bold'
  },
  grade: {
    width: '100%',
    height: 50,
    justifyContent: 'center',
    alignItems: 'center',
    borderRadius: 10,
    marginTop: 10
  },
  grade2: {
    width: '25%',
    height: 50,
    justifyContent: 'center',
    alignItems: 'center',
    borderRadius: 10,
    marginTop: 10
  },
  view: {
    flex: 1,
    justifyContent: 'center',
    alignItems: 'center',
  },
  title: {
    fontSize: 40,
    fontWeight: "bold",
    color: "#08d4c4",
    marginBottom: 50
  },
})
export default Profile;