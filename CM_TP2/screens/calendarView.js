import * as React from 'react';
import { useState, useEffect } from 'react';
import { Text, View, StyleSheet, TextInput, TouchableOpacity, FlatList, LogBox } from 'react-native';
import AsyncStorage from '@react-native-async-storage/async-storage';
import DialogInput from 'react-native-dialog-input';
import LinearGradient from 'react-native-linear-gradient';
LogBox.ignoreAllLogs();

const CalendarView = ({ navigation }) => {

  const [id_count, setIdCount] = useState(1);
  const [textInput_Holder, setTextInput] = useState("");
  const [arrayHolder, setArrayHolder] = useState([]);
  const [visible, setVisible] = useState(false);
  const [itemName, setItemName] = useState("");

  useEffect(() => {
    async function fetchCredentials() {
      const jsonValue = await AsyncStorage.getItem('@cal_storage');
      if (JSON.parse(jsonValue) != null) {
        setArrayHolder(JSON.parse(jsonValue));
      }
      else
        setArrayHolder([]);
    }
    fetchCredentials();
  }, []);

  useEffect(() => {
    storeData(arrayHolder);
  });

  const showDialog = (oldName) => {
    setItemName(oldName);
    setVisible(true);
  };

  const handleCancel = () => {
    setVisible(false);
  };

  const handleEdit = (newName) => {
    let oldName = itemName;
    let arrayCopy = arrayHolder;
    let index = arrayCopy.findIndex(el => el.title === oldName);
    arrayCopy[index] = { title: newName };
    setArrayHolder(arrayCopy);
    setVisible(false);
  };

  const removeCal = (calKey) => {
    handleRemove(calKey);
  };

  const handleRemove = (calKey) => {
    let key = calKey;
    let arrayCopy = arrayHolder;
    let index = arrayCopy.findIndex(el => el.key === key);
    arrayCopy.splice(index, 1);
    setArrayHolder(arrayCopy);
    storeData(arrayHolder);
  };


  const joinData = () => {
    setArrayHolder(arrayHolder.concat({ key: id_count, title: textInput_Holder }));
    setIdCount(id_count + 1);
  }

  const storeData = async (value) => {
    try {
      const jsonValue = JSON.stringify(value)
      await AsyncStorage.setItem('@cal_storage', jsonValue)
    } catch (e) {
      console.log(e)
    }
  }


  return (
    <View
      style={{
        flex: 1,
        justifyContent: "center",
        alignItems: "center"
      }}>
      <View style={styles.view}>
        <Text style={styles.title}>Home</Text>
        <TouchableOpacity style={styles.grade3} onPress={() => navigation.navigate('Profile')}>
          <LinearGradient colors={['#08d4c4', '#01ab9d']} style={styles.grade}>
            <Text style={styles.button}> Profile </Text>
          </LinearGradient>
        </TouchableOpacity>
        <TextInput style={styles.input}
          placeholder="Enter calendar name"
          onChangeText={data => setTextInput(data)}
          underlineColorAndroid='transparent'
        />
        <TouchableOpacity style={styles.grade} onPress={joinData} activeOpacity={0.7}>
          <LinearGradient colors={['#08d4c4', '#01ab9d']} style={styles.grade}>
            <Text style={styles.button}> Add New Calendar </Text>
          </LinearGradient>
        </TouchableOpacity>
        <Text>{"\n"}</Text>
        <FlatList
          data={arrayHolder}
          width='100%'
          extraData={arrayHolder}
          keyExtractor={(index) => index.toString()}
          renderItem={({ item }) =>
            <View style={{
              width: '100%',
              flex: 1,
              flexDirection: "row",
              justifyContent: "center",
              alignItems: "center",
            }}>
              <Text onPress={() =>
                navigation.navigate('Agenda', {
                  calendarId: item.key
                })}
                style={{
                  width: 150,
                  backgroundColor: "white",
                  fontSize: 16,
                  borderRadius: 10,
                  padding: 10,
                  textAlign: "center",
                  marginTop: 20,
                  margin: 10
                }}
              > {item.title} </Text>

              <TouchableOpacity style={styles.grade2} onPress={() => { showDialog(item.title) }} activeOpacity={0.7}>
                <LinearGradient colors={['#08d4c4', '#01ab9d']} style={styles.grade}>
                  <Text style={styles.button}> Edit </Text>
                </LinearGradient>
              </TouchableOpacity>
              <Text> </Text>
              <TouchableOpacity style={styles.grade2} onPress={() => { removeCal(item.key) }} activeOpacity={0.7}>
                <LinearGradient colors={['#08d4c4', '#01ab9d']} style={styles.grade}>
                  <Text style={styles.button}> Remove </Text>
                </LinearGradient>
              </TouchableOpacity>

              <DialogInput isDialogVisible={visible}
                title={"Edit Calender Name"}
                message={"Insert the calender's new name."}
                hintInput={"New Name Here"}
                submitInput={(inputText) => { handleEdit(inputText) }}
                closeDialog={handleCancel}>
              </DialogInput>

            </View>
          } />
      </View>
    </View>
  );
}

const styles = StyleSheet.create({
  title: {
    fontSize: 40,
    fontWeight: "bold",
    color: "#08d4c4",
    marginBottom: 50
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
    margin: 10,
    justifyContent: 'center',
    alignItems: 'center',
    borderRadius: 10
  },
  grade2: {
    width: '25%',
    height: 50,
    justifyContent: 'center',
    alignItems: 'center',
    borderRadius: 10,
    marginTop: 10
  },
  grade3: {
    width: '100%',
    height: 50,
    marginBottom: 80,
    justifyContent: 'center',
    alignItems: 'center',
    borderRadius: 10
  },
  view: {
    justifyContent: 'center',
    alignItems: 'center',
    marginTop: 50
  },
  input: {
    backgroundColor: "white",
    borderRadius: 10,
    padding: 10
  }
})

export default CalendarView;