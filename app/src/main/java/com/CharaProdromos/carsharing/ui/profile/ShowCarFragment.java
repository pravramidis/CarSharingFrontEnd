//package com.CharaProdromos.carsharing.ui.profile;
//
//import android.os.Bundle;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import androidx.fragment.app.Fragment;
//import androidx.fragment.app.FragmentTransaction;
//import androidx.annotation.NonNull;
//
//import com.CharaProdromos.carsharing.GlobalVariables;
//import com.CharaProdromos.carsharing.R;
//import com.CharaProdromos.carsharing.databinding.FragmentShowCarBinding;
//import com.android.volley.Request;
//import com.android.volley.Response;
//import com.android.volley.VolleyError;
//import com.android.volley.toolbox.JsonObjectRequest;
//import com.android.volley.toolbox.Volley;
//
//import org.json.JSONException;
//import org.json.JSONObject;
//import org.w3c.dom.Text;
//
//public class ShowCarFragment extends Fragment{
//
//    TextView title;
//    TextView plate;
//    TextView type;
//    TextView fuelType;
//    TextView gearbox;
//    TextView capacity;
//
//    private FragmentShowCarBinding binding;
//
//    public View onCreateView(@NonNull LayoutInflater inflater,
//                             ViewGroup container, Bundle savedInstanceState) {
//
//
//        binding = FragmentShowCarBinding.inflate(inflater, container, false);
//        View root = binding.getRoot();
//
//        title = root.findViewById(R.id.carTitle);
//        plate = root.findViewById(R.id.plateText);
//        type = root.findViewById(R.id.typeText);
//        fuelType = root.findViewById(R.id.fuelTypeText);
//        gearbox = root.findViewById(R.id.gearboxText);
//        capacity = root.findViewById(R.id.capacityText);
//
//        String carPlate = GlobalVariables.getInstance().getPlateNumber();
//        plate.setText(carPlate);
//        httpRequestCarInfo(carPlate);
//
//
//
//
//        return root;
//    }
//
//
//    private void httpRequestCarInfo(String plateNum) {
//        JSONObject jsonBody = new JSONObject();
//        try {
//            jsonBody.put("Plate_number", plateNum);
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//        String serverAddress = this.getString(R.string.serverAddress);
//        System.out.println(jsonBody.toString());
//        String url = serverAddress + "/vehicles/carInfo";
//        System.out.println(url);
//
//        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, jsonBody,
//                new Response.Listener<JSONObject>() {
//                    @Override
//                    public void onResponse(JSONObject response) {
//                        System.out.println(response.toString());
//
//                        try {
//                            String carCapacity = response.getString("Passenger_capacity");
//                            String carGearbox = response.getString("Gearbox");
//                            String carBrand = response.getString("Brand");
//                            String carModel = response.getString("Model");
//                            String carType = response.getString("Type");
//                            String carFuelType = response.getString("Fuel_Type");
//                            if (carGearbox.equals("1")) {
//                                carGearbox = "Manual";
//                            }
//                            else {
//                                carGearbox = "Automatic";
//                            }
//
//                            String carTitle = carBrand + " " +carModel;
//                            title.setText(carTitle);
//                            capacity.setText(carCapacity);
//                            gearbox.setText(carGearbox);
//                            type.setText(carType);
//                            fuelType.setText(carFuelType);
//                            plate.setText(plateNum);
//                        }
//                        catch (Exception exception) {
//
//                            System.out.println("Error in profile info");
//                            exception.printStackTrace();
//                        }
//                    }
//                },
//                new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//                        // Handle errors
//                        // You can log the error or show a message to the user
//
//                        String text = "Fail"+ error.toString();
//                        Toast toast = Toast.makeText(getActivity().getApplicationContext(),text, Toast.LENGTH_LONG);
//                        System.out.println("Fail"+ error.toString());
//                        toast.show();
//                    }
//                });
//        Volley.newRequestQueue(getActivity().getApplicationContext()).add(jsonObjectRequest);
//    }
//}
