package zychgrzegorz.btkeyboardserver;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattServer;
import android.bluetooth.BluetoothGattServerCallback;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothManager;
import android.bluetooth.le.AdvertiseCallback;
import android.bluetooth.le.AdvertiseData;
import android.bluetooth.le.AdvertiseSettings;
import android.bluetooth.le.BluetoothLeAdvertiser;
import android.content.Intent;
import android.os.Bundle;
import android.os.Debug;
import android.os.ParcelUuid;
import android.preference.Preference;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.lang.reflect.InvocationTargetException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.UUID;

public class ConnectActivity extends Activity {

    //public static final String INPUT_CHARACTERISTIC_UUID_STRING = "715524a1-b3b8-4c2d-b587-2865114a1a08";
    public static final String MODE_CHARACTERISTIC_UUID_STRING = "91109139-7195-4fa7-bb99-b187fd4c08a6";
    public static final String SERVICE_UUID_STRING = "66255a21-e815-4e64-8e0e-8ae4f6603009";
    public static final String CLIENT_CHARACTERISTIC_CONFIG_UUID_STRING = "00002902-0000-1000-8000-00805f9b34fb";
    //public static final UUID INPUT_CHARACTERISTIC_UUID = UUID.fromString(INPUT_CHARACTERISTIC_UUID_STRING);
    public static final UUID MODE_CHARACTERISTIC_UUID = UUID.fromString(MODE_CHARACTERISTIC_UUID_STRING);
    public static final UUID SERVICE_UUID = UUID.fromString(SERVICE_UUID_STRING);
    public static final UUID CLIENT_CHARACTERISTIC_CONFIG_UUID = UUID.fromString(CLIENT_CHARACTERISTIC_CONFIG_UUID_STRING);
    private static ConnectActivity thisActivity;
    private boolean modeSubscribed = false;
    private static BluetoothAdapter mBluetoothAdapter;
    private static BluetoothLeAdvertiser mBluetoothLeAdvertiser;
    private static BluetoothGattService gattService;
    private static BluetoothManager bluetoothManager;
    private static BluetoothGattServerCallback serverCallback;
    private static BluetoothGattServer gattServer;
    private final int REQUEST_CODE_ENABLE_BT = 1;
    private TextView logView;
    private HashMap<Enum,BluetoothGattCharacteristic> inputCharacteristics;
    private AdvertiseCallback advertiseCallback;
    private AdvertiseSettings settings;
    private AdvertiseData data;
    private static BluetoothGattCharacteristic modeCharacteristic;
    private BluetoothDevice connectedDevice;

    public void setMode(MODES mode) throws InvocationTargetException {
        modeCharacteristic.setValue(new byte[]{mode.mode()});

        try {
            gattServer.notifyCharacteristicChanged(connectedDevice, modeCharacteristic, false);
        }catch(Exception e){}
    }

    public void changeCharacteristicValue(Enum characteristicEnum,String value){
        BluetoothGattCharacteristic characteristic = inputCharacteristics.get(characteristicEnum);
        characteristic.setValue(value);
        Log.d("CHVAL",MessageFormat.format("Characteristic: {0} value: {1}",characteristicEnum.name(),value));

        try{
        gattServer.notifyCharacteristicChanged(connectedDevice,characteristic,false);
        }catch (Exception e){}
    }

    public void changeKeyboardCharacteristicValue(HashSet<Short> pressedKeys){
        BluetoothGattCharacteristic characteristic = inputCharacteristics.get(KEYBOARD_CHARACTERISTICS_UUIDS.ScanCode);
        short[] shorts = new short[pressedKeys.size()];
        int i =0;
        for(Short s : pressedKeys){
            shorts[i] = s;
            i++;
        }
        byte[] bytes = new byte[shorts.length*2];
        ByteBuffer.wrap(bytes).order(ByteOrder.LITTLE_ENDIAN).asShortBuffer().put(shorts);
        characteristic.setValue(bytes);
        gattServer.notifyCharacteristicChanged(connectedDevice,characteristic,false);
    }

    public static ConnectActivity getThisActivity() {
        return thisActivity;
    }

    public enum MODES{
        KEYBOARD((byte)1),
        MOUSE((byte)2),
        GAMEPAD((byte)3);

        private final byte mode;

        MODES(byte mode){
            this.mode = mode;
        }
        public byte mode(){
            return mode;
        };
    }

    public enum GAMEPAD_CHARACTERISTICS_UUIDS{
        Buttons(UUID.fromString("5906134f-0a14-4eb8-871f-b67885d2733a")),
        LeftThumbstickX(UUID.fromString("5ed38605-4a0b-4039-bc64-2d0bd7e28274")),
        LeftThumbstickY(UUID.fromString("b61e1d2f-61fb-4b32-b693-439c7dc94c65")),
        LeftTrigger(UUID.fromString("389b8d8a-fad6-4be6-9a9a-8962a42d95eb")),
        RightThumbstickX(UUID.fromString("2f1ea192-4c39-4a2c-9371-63c6e12cac14")),
        RightThumbstickY(UUID.fromString("9db40e09-f3a8-4eb7-9e85-4aab96d2db14")),
        RightTrigger(UUID.fromString("1b6be6a0-d72c-4eb6-b1b1-053359043cf3"))
        ;


        private final UUID uuid;

        GAMEPAD_CHARACTERISTICS_UUIDS(UUID uuid){this.uuid = uuid;}
        public UUID getUuid() {return uuid;}
    }

    public enum KEYBOARD_CHARACTERISTICS_UUIDS{
        ScanCode(UUID.fromString("f4509bd6-3c64-4a0e-b43d-2beb8bd44072")),
       /* Shift(UUID.fromString("e25c8f15-2d48-46e5-8023-eac6dfe8a2b6")),
        Alt(UUID.fromString("7fa79b9e-4871-4eca-81ad-bf92135e16b0"))*/
        ;


        private final UUID uuid;

        KEYBOARD_CHARACTERISTICS_UUIDS(UUID uuid){this.uuid = uuid;}
        public UUID getUuid() {return uuid;}
    }

    public enum MOUSE_CHARACTERISTICS_UUIDS{
        DeltaX(UUID.fromString("c1463681-fa4b-4c89-978d-cac6ee299dd9")),
        DeltaY(UUID.fromString("8994394d-2e98-4b9f-9638-8d9d8f7bc2d7")),
        MouseOptions(UUID.fromString("a73e4e39-a963-4523-bd2d-3d74ec4a0a08"))
        ;


        private final UUID uuid;

        MOUSE_CHARACTERISTICS_UUIDS(UUID uuid){this.uuid = uuid;}
        public UUID getUuid() {return uuid;}
    }




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("ON","OnCreate invoked");
        thisActivity = this;
        setContentView(R.layout.activity_connect);

        logView = findViewById(R.id.logView);

        connectedDevice = null;
        bluetoothManager =
                (BluetoothManager) getSystemService(BLUETOOTH_SERVICE);
        mBluetoothAdapter = bluetoothManager.getAdapter();
        inputCharacteristics = new HashMap<>();

    }

    @Override
    protected void onResume() {
        super.onResume();

        Log.d("ON","OnResume invoked");
        if (mBluetoothAdapter == null || !mBluetoothAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, REQUEST_CODE_ENABLE_BT);

        }

    }
    @Override
    protected void onDestroy() {
        super.onDestroy();

        Log.d("ON","OnDestroy invoked");
        if (gattServer != null) {
            gattServer.close();
            gattServer = null;
        }
        if (mBluetoothLeAdvertiser != null) {
            mBluetoothLeAdvertiser.stopAdvertising(advertiseCallback);
            mBluetoothLeAdvertiser = null;
        }
    }

    @Override
    protected void onStop() {
        super.onStop();

        Log.d("ON","OnStop invoked");
/*        if (gattServer != null) {
            gattServer.close();
            gattServer = null;
        }*/
        if (mBluetoothLeAdvertiser != null) {
            mBluetoothLeAdvertiser.stopAdvertising(advertiseCallback);
            mBluetoothLeAdvertiser = null;
        }
    }

    private void setupServer() {

        if (gattServer != null) {
            return;
        }


        mBluetoothLeAdvertiser = mBluetoothAdapter.getBluetoothLeAdvertiser();

        logEvent("\nName " + mBluetoothAdapter.getName());
        logEvent("\nMultiAdvertisement " + mBluetoothAdapter.isMultipleAdvertisementSupported());

        serverCallback = new BluetoothGattServerCallback() {
            @Override
            public void onServiceAdded(int status, BluetoothGattService service) {
                super.onServiceAdded(status, service);

                //logView.setText(logView.getText()+"\nService added " + service.getUuid().toString() + " Status:" + status);
                String message = "\nService added " + service.getUuid().toString() + " Status:" + status;

                logEvent(message);
            }

            @Override
            public void onConnectionStateChange(BluetoothDevice device, int status, int newState) {
                super.onConnectionStateChange(device, status, newState);

                String message = "\nConnection state changed " + device.getName() +
                        " " + status + " " + newState;
                logEvent(message);
            }

            @Override
            public void onCharacteristicWriteRequest(BluetoothDevice device, int requestId, BluetoothGattCharacteristic characteristic, boolean preparedWrite, boolean responseNeeded, int offset, byte[] value) {
                super.onCharacteristicWriteRequest(device, requestId, characteristic, preparedWrite, responseNeeded, offset, value);
                logEvent("Write request from " + device.getName() + "to " + characteristic.getUuid());

                switch (characteristic.getUuid().toString()){
                    case MODE_CHARACTERISTIC_UUID_STRING:

                        break;
                    default:
                        logEvent(MessageFormat.format("Write request for an unsupported characteristic {0}",characteristic.getUuid().toString()));
                        break;
                }
            }

            @Override
            public void onCharacteristicReadRequest(BluetoothDevice device, int requestId, int offset, BluetoothGattCharacteristic characteristic) {
                super.onCharacteristicReadRequest(device, requestId, offset, characteristic);

                gattServer.sendResponse(device,requestId,BluetoothGatt.GATT_SUCCESS,0,characteristic.getValue());

            }

            @Override
            public void onDescriptorWriteRequest(BluetoothDevice device, int requestId, BluetoothGattDescriptor descriptor, boolean preparedWrite, boolean responseNeeded, int offset, byte[] value) {
                super.onDescriptorWriteRequest(device, requestId, descriptor, preparedWrite, responseNeeded, offset, value);

                logEvent(MessageFormat.format("Descriptor write request for uuid {0}", descriptor.getUuid().toString()));
                if (descriptor.getUuid().equals(CLIENT_CHARACTERISTIC_CONFIG_UUID)) {
                    if (connectedDevice == null || device.getAddress().equals(connectedDevice.getAddress())) {
                        descriptor.setValue(value);
                        if (descriptor.getCharacteristic().getUuid().equals(MODE_CHARACTERISTIC_UUID))
                            setModeSubscribed(true);
                        if (connectedDevice == null)
                            setConnectedDevice(device);
                        if (responseNeeded) {
                            gattServer.sendResponse(device, requestId, BluetoothGatt.GATT_SUCCESS, 0, null);
                            logEvent(MessageFormat.format("Device {0} subscribed to characteristic {1}", device.getName(), descriptor.getCharacteristic().getUuid().toString()));

                        }
                    }
                } else {
                    logEvent(MessageFormat.format("Attempts to subscribe from multiple devices. Connected device {0} new device {1}",
                            connectedDevice.getName(), device.getName()));
                }
            }


            @Override
            public void onNotificationSent(BluetoothDevice device, int status) {
                super.onNotificationSent(device, status);
                logEvent("Notification sent to " + device.getName() + " status: " + status);
            }
        };


        advertiseCallback = new AdvertiseCallback() {

            @Override
            public void onStartSuccess(AdvertiseSettings settingsInEffect) {
                super.onStartSuccess(settingsInEffect);
                String message = "\nAdvertise start success " + settingsInEffect.isConnectable();
                logEvent(message);

            }

            @Override
            public void onStartFailure(int errorCode) {
                super.onStartFailure(errorCode);
                String message = "\nAdvertise start failure " + errorCode;

                logEvent(message);
            }

        };

        gattService = new BluetoothGattService(SERVICE_UUID, BluetoothGattService.SERVICE_TYPE_PRIMARY);

        setupCharacteristics();

        settings = new AdvertiseSettings.Builder()
                .setAdvertiseMode(AdvertiseSettings.ADVERTISE_MODE_LOW_LATENCY)
                .setConnectable(true)
                .setTimeout(0)
                .setTxPowerLevel(AdvertiseSettings.ADVERTISE_TX_POWER_HIGH)
                .build();

        ParcelUuid parcelUuid = new ParcelUuid(SERVICE_UUID);

        data = new AdvertiseData.Builder()
                .setIncludeDeviceName(true)
                .addServiceUuid(parcelUuid)
                .build();


        startAdvertising();
    }

    private void setupCharacteristics() {
        //create gamepad characteristics
        for(GAMEPAD_CHARACTERISTICS_UUIDS characteristicData : GAMEPAD_CHARACTERISTICS_UUIDS.values()){
            BluetoothGattCharacteristic characteristic = new BluetoothGattCharacteristic(characteristicData.getUuid(),
                    BluetoothGattCharacteristic.PROPERTY_NOTIFY,BluetoothGattCharacteristic.PERMISSION_READ);

            BluetoothGattDescriptor inputConfigDescriptor = new BluetoothGattDescriptor(CLIENT_CHARACTERISTIC_CONFIG_UUID,
                    BluetoothGattDescriptor.PERMISSION_READ|BluetoothGattDescriptor.PERMISSION_WRITE);

            characteristic.addDescriptor(inputConfigDescriptor);

            inputCharacteristics.put(characteristicData,characteristic);
            gattService.addCharacteristic(characteristic);
        }

        //create keyboard characteristics
        for(KEYBOARD_CHARACTERISTICS_UUIDS characteristicData : KEYBOARD_CHARACTERISTICS_UUIDS.values()){
            BluetoothGattCharacteristic characteristic = new BluetoothGattCharacteristic(characteristicData.getUuid(),
                    BluetoothGattCharacteristic.PROPERTY_NOTIFY | BluetoothGattCharacteristic.PROPERTY_READ,
                    BluetoothGattCharacteristic.PERMISSION_READ);

            BluetoothGattDescriptor inputConfigDescriptor = new BluetoothGattDescriptor(CLIENT_CHARACTERISTIC_CONFIG_UUID,
                    BluetoothGattDescriptor.PERMISSION_READ|BluetoothGattDescriptor.PERMISSION_WRITE);

            characteristic.addDescriptor(inputConfigDescriptor);

            inputCharacteristics.put(characteristicData,characteristic);
            gattService.addCharacteristic(characteristic);
        }

        //create mouse characteristics
        for(MOUSE_CHARACTERISTICS_UUIDS characteristicData : MOUSE_CHARACTERISTICS_UUIDS.values()){
            BluetoothGattCharacteristic characteristic = new BluetoothGattCharacteristic(characteristicData.getUuid(),
                    BluetoothGattCharacteristic.PROPERTY_NOTIFY,BluetoothGattCharacteristic.PERMISSION_READ);

            BluetoothGattDescriptor inputConfigDescriptor = new BluetoothGattDescriptor(CLIENT_CHARACTERISTIC_CONFIG_UUID,
                    BluetoothGattDescriptor.PERMISSION_READ|BluetoothGattDescriptor.PERMISSION_WRITE);

            characteristic.addDescriptor(inputConfigDescriptor);

            inputCharacteristics.put(characteristicData,characteristic);
            gattService.addCharacteristic(characteristic);
        }

        //create mode characteristic
        BluetoothGattDescriptor modeConfigDescriptor = new BluetoothGattDescriptor(CLIENT_CHARACTERISTIC_CONFIG_UUID,
                BluetoothGattDescriptor.PERMISSION_READ|BluetoothGattDescriptor.PERMISSION_WRITE);

        modeCharacteristic = new BluetoothGattCharacteristic(
                MODE_CHARACTERISTIC_UUID, BluetoothGattCharacteristic.PROPERTY_INDICATE|BluetoothGattCharacteristic.PROPERTY_READ|BluetoothGattCharacteristic.PROPERTY_WRITE_NO_RESPONSE ,
                BluetoothGattCharacteristic.PERMISSION_READ|BluetoothGattCharacteristic.PERMISSION_WRITE);

        modeCharacteristic.setWriteType(BluetoothGattCharacteristic.WRITE_TYPE_NO_RESPONSE);
        modeCharacteristic.addDescriptor(modeConfigDescriptor);
        gattService.addCharacteristic(modeCharacteristic);
    }

    private void startAdvertising() {

        gattServer = bluetoothManager.openGattServer(this, serverCallback);
        gattServer.clearServices();
        gattServer.addService(gattService);

        mBluetoothLeAdvertiser.startAdvertising(settings, data, advertiseCallback);
    }

    private void logEvent(final String message) {
        if(logView!=null) {
            final String finalMessage;
            if (!message.startsWith("\n")) {
                finalMessage = "\n" + message;
            } else {
                finalMessage = message;
            }
            ConnectActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    logView.append(finalMessage);
                }
            });
        }
    }

    private void clearLog(){
        ConnectActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                logView.setText("");
            }
        });
    }

    public void clickConnect(View view){
        findViewById(R.id.progressBar).setVisibility(View.VISIBLE);
        setupServer();
    }

    public void setConnectedDevice(BluetoothDevice connectedDevice) {
        if(this.connectedDevice == null) {
            this.connectedDevice = connectedDevice;
            logEvent(MessageFormat.format("Device {0} connected",connectedDevice.getName()));
        }
        else
            logEvent("Cannot connect new device. Device already connected");
    }


    public void setModeSubscribed(boolean modeSubscribed) {
        this.modeSubscribed = modeSubscribed;
        if(this.modeSubscribed && connectedDevice!=null){
            mBluetoothLeAdvertiser.stopAdvertising(advertiseCallback);
            Intent intent = new Intent(this,InputChoiceActivity.class);
            startActivity(intent);
        }
    }

}
