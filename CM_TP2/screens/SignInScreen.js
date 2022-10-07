import React from 'react';
import {
    View,
    Text,
    TouchableOpacity,
    TextInput,
    Platform,
    StyleSheet,
    StatusBar
} from 'react-native';
import * as Animatable from 'react-native-animatable';
import LinearGradient from 'react-native-linear-gradient';
import FontAwesome from 'react-native-vector-icons/FontAwesome';
import Feather from 'react-native-vector-icons/Feather';
import DialogInput from 'react-native-dialog-input';

import auth from '@react-native-firebase/auth';

const SignInScreen = ({ navigation }) => {

    const [visible, setVisible] = React.useState(false);

    const showDialog = () => {
        setVisible(true);
    };

    const handleCancel = () => {
        setVisible(false);
    };

    const recoverPassword = async (input) => {
        if (input.length !== 0) {
            try {
                await auth().sendPasswordResetEmail(input)
                    .catch(error => {
                        console.log('Something went wrong with password recover: ', error);
                    });
            } catch (e) {
                console.log(e);
            }
        }
        setVisible(false);
    }

    const [data, setData] = React.useState({
        email: '',
        password: '',
        secureTextEntry: true,
    });

    const login = async () => {
        if (data.email.length != 0 && data.password.length != 0) {
            try {
                await auth().signInWithEmailAndPassword(data.email, data.password).then(() => { navigation.navigate('Calendar') })
                    .catch(error => {
                        console.log('Something went wrong with sign in: ', error);
                    });
            } catch (e) {
                console.log(e);
            }
        }
    }


    const handleEmailChange = (val) => {
        setData({
            ...data,
            email: val,
        });
    }

    const handlePasswordChange = (val) => {
        setData({
            ...data,
            password: val,
        });
    }

    const updateSecureTextEntry = () => {
        setData({
            ...data,
            secureTextEntry: !data.secureTextEntry
        });
    }

    return (
        <View style={styles.container}>
            <StatusBar backgroundColor='#009387' barStyle="light-content" />
            <View style={styles.header}>
                <Text style={styles.text_header}>Welcome!</Text>
            </View>
            <Animatable.View
                style={styles.footer}
            >
                <Text style={styles.text_footer}>E-Mail</Text>
                <View style={styles.action}>
                    <FontAwesome
                        name="user-o"
                        color="#05375a"
                        size={20}
                    />
                    <TextInput
                        placeholder="Your E-Mail"
                        style={styles.textInput}
                        autoCapitalize="none"
                        onChangeText={(val) => handleEmailChange(val)}
                    />
                </View>


                <Text style={[styles.text_footer, {
                    marginTop: 35
                }]}>Password</Text>
                <View style={styles.action}>
                    <Feather
                        name="lock"
                        color="#05375a"
                        size={20}
                    />
                    <TextInput
                        placeholder="Your Password"
                        secureTextEntry={data.secureTextEntry ? true : false}
                        style={styles.textInput}
                        autoCapitalize="none"
                        onChangeText={(val) => handlePasswordChange(val)}
                    />
                    <TouchableOpacity
                        onPress={updateSecureTextEntry}
                    >
                        {data.secureTextEntry ?
                            <Feather
                                name="eye-off"
                                color="grey"
                                size={20}
                            />
                            :
                            <Feather
                                name="eye"
                                color="grey"
                                size={20}
                            />
                        }
                    </TouchableOpacity>
                </View>


                <TouchableOpacity
                    onPress={() => showDialog()}
                >
                    <Text style={{ color: '#009387', marginTop: 15 }}>Forgot password?</Text>
                </TouchableOpacity>
                <View style={styles.button}>
                    <TouchableOpacity
                        style={styles.signIn}
                        onPress={() => login()}
                    >
                        <LinearGradient
                            colors={['#08d4c4', '#01ab9d']}
                            style={styles.signIn}
                        >
                            <Text style={[styles.textSign, {
                                color: '#fff'
                            }]}>Sign In</Text>
                        </LinearGradient>
                    </TouchableOpacity>

                    <TouchableOpacity
                        onPress={() => navigation.navigate('SignUpScreen')}
                        style={[styles.signIn, {
                            borderColor: '#009387',
                            borderWidth: 1,
                            marginTop: 15
                        }]}
                    >
                        <Text style={[styles.textSign, {
                            color: '#009387'
                        }]}>Sign Up</Text>
                    </TouchableOpacity>
                </View>
            </Animatable.View>

            <DialogInput isDialogVisible={visible}
                title={"Recover Password"}
                message={"Account E-Mail"}
                submitInput={(inputText) => { recoverPassword(inputText) }}
                closeDialog={handleCancel}>
            </DialogInput>

        </View>
    );
};

export default SignInScreen;

const styles = StyleSheet.create({
    container: {
        flex: 1,
        backgroundColor: '#009387'
    },
    header: {
        flex: 1,
        justifyContent: 'flex-end',
        paddingHorizontal: 20,
        paddingBottom: 20,
        marginTop: -100
    },
    footer: {
        flex: 3,
        backgroundColor: '#fff',
        borderTopLeftRadius: 30,
        borderTopRightRadius: 30,
        paddingHorizontal: 20,
        paddingVertical: 30
    },
    text_header: {
        color: '#fff',
        fontWeight: 'bold',
        fontSize: 30
    },
    text_footer: {
        color: '#05375a',
        fontSize: 18
    },
    action: {
        flexDirection: 'row',
        marginTop: 10,
        borderBottomWidth: 1,
        borderBottomColor: '#f2f2f2',
        paddingBottom: 5
    },
    actionError: {
        flexDirection: 'row',
        marginTop: 10,
        borderBottomWidth: 1,
        borderBottomColor: '#FF0000',
        paddingBottom: 5
    },
    textInput: {
        flex: 1,
        marginTop: Platform.OS === 'ios' ? 0 : -12,
        paddingLeft: 10,
        color: '#05375a',
    },
    errorMsg: {
        color: '#FF0000',
        fontSize: 14,
    },
    button: {
        alignItems: 'center',
        marginTop: 50
    },
    signIn: {
        width: '100%',
        height: 50,
        justifyContent: 'center',
        alignItems: 'center',
        borderRadius: 10
    },
    textSign: {
        fontSize: 18,
        fontWeight: 'bold'
    }
});
