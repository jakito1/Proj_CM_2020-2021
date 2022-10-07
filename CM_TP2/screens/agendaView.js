import * as React from 'react';
import { useState } from 'react';
import { Text, View, TouchableOpacity, StyleSheet } from 'react-native';
import { Agenda } from 'react-native-calendars';
import DialogInput from 'react-native-dialog-input';
import LinearGradient from 'react-native-linear-gradient';

const testIDs = require('./testIDs');

const AgendaView = ({ navigation }) => {

  const [items, setItems] = useState({});
  const [visible, setVisible] = useState(false);
  const [visible2, setVisible2] = useState(false);

  const [selected, setSelected] = useState({});

  const showDialogAdd = () => {
    setVisible(true);
  };

  const showDialogEdit = () => {
    setVisible2(true);
  };

  const handleCancelAdd = () => {
    setVisible(false);
  };

  const handleCancelEdit = () => {
    setVisible2(false);
  };

  const handleEdit = () => {
    setVisible2(false);
  };


  const handleAdd = (input) => {
    if (!items[selected.dateString]) {
      items[selected.dateString] = [];
    }
    items[selected.dateString].push({ name: input, height: 100 });

    const newItems = {};
    Object.keys(items).forEach(key => {
      newItems[key] = items[key];
    });
    setItems(newItems)

    setVisible(false);
  };

  const handleRemove = () => {
    items[selected.dateString] = null;
  };


  const renderItem = (item) => {
    return (
      <TouchableOpacity style={styles.item}
        testID={testIDs.agenda.ITEM}
        onPress={() => showDialogEdit()}
      >
        <Text style={styles.text}>{item.name}</Text>
      </TouchableOpacity>
    );
  }

  const styles = StyleSheet.create({
    button: {
      fontSize: 16,
      color: "white",
      padding: 2,
      textAlign: "center",
      fontWeight: 'bold'
    },
    grade: {
      height: 50,
      margin: 0,
      justifyContent: 'center',
      alignItems: 'center',
      borderRadius: 10
    },
    agenda: {
      marginBottom: 10
    },
    item: {
      backgroundColor: "white",
      borderRadius: 5,
      padding: 40,
      marginTop: 20,
      width: 250,
      textAlign: "center"
    },
    text: {
      fontSize: 16
    }
  })

  return (
    <View style={{ flex: 1 }}>
      <Agenda style={styles.agenda} items={items} renderItem={renderItem} onDayPress={(day) => setSelected(day)}
        pastScrollRange={10} futureScrollRange={10} />

      <TouchableOpacity style={styles.button} onPress={() => { showDialogAdd() }} activeOpacity={0.7}>
        <LinearGradient colors={['#08d4c4', '#01ab9d']} style={styles.grade}>
          <Text style={styles.button}> Add Event </Text>
        </LinearGradient>
      </TouchableOpacity>
      <TouchableOpacity style={styles.button} onPress={() => { handleRemove() }} activeOpacity={0.7}>
        <LinearGradient colors={['#08d4c4', '#01ab9d']} style={styles.grade}>
          <Text style={styles.button}> Remove Events </Text>
        </LinearGradient>
      </TouchableOpacity>
      <TouchableOpacity style={styles.button} onPress={() => navigation.navigate('Calendar')} activeOpacity={0.7}>
        <LinearGradient colors={['#08d4c4', '#01ab9d']} style={styles.grade}>
          <Text style={styles.button}> Home </Text>
        </LinearGradient>
      </TouchableOpacity>


      <DialogInput isDialogVisible={visible}
        title={"Add Event"}
        message={"Date: " + selected.dateString + "\nDescription: "}
        submitInput={(inputText) => { handleAdd(inputText) }}
        closeDialog={handleCancelAdd}>
      </DialogInput>
      <DialogInput isDialogVisible={visible2}
        title={"Edit Event"}
        message={"New description: "}
        submitInput={(inputText) => { handleEdit(inputText) }}
        closeDialog={handleCancelEdit}>
      </DialogInput>
    </View>
  )
}
export default AgendaView;