package edu.kvcc.cis298.cis298assignment4;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.provider.ContactsContract.CommonDataKinds.Email;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;

/**
 * Created by David Barnes on 11/3/2015.
 */
public class BeverageFragment extends Fragment {

    //String key that will be used to send data between fragments
    private static final String ARG_BEVERAGE_ID = "crime_id";
    private static final int REQUEST_CODE = 100;

    //private class level vars for the model properties
    private EditText mId;
    private EditText mName;
    private EditText mPack;
    private EditText mPrice;
    private CheckBox mActive;
    private Button mContact;
    private Button mEmail;
    private String mContactName;
    private String mContactAddress;
    private String mContactSubject;
    private String mContactBody;

    //Private var for storing the beverage that will be displayed with this fragment
    private Beverage mBeverage;

    //Public method to get a properly formatted version of this fragment
    public static BeverageFragment newInstance(String id) {
        //Make a bungle for fragment args
        Bundle args = new Bundle();
        //Put the args using the key defined above
        args.putString(ARG_BEVERAGE_ID, id);

        //Make the new fragment, attach the args, and return the fragment
        BeverageFragment fragment = new BeverageFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //When created, get the beverage id from the fragment args.
        String beverageId = getArguments().getString(ARG_BEVERAGE_ID);
        //use the id to get the beverage from the singleton
        mBeverage = BeverageCollection.get(getActivity()).getBeverage(beverageId);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //Use the inflator to get the view from the layout
        View view = inflater.inflate(R.layout.fragment_beverage, container, false);

        //Get handles to the widget controls in the view
        mId = (EditText) view.findViewById(R.id.beverage_id);
        mName = (EditText) view.findViewById(R.id.beverage_name);
        mPack = (EditText) view.findViewById(R.id.beverage_pack);
        mPrice = (EditText) view.findViewById(R.id.beverage_price);
        mActive = (CheckBox) view.findViewById(R.id.beverage_active);
        mContact = (Button) view.findViewById(R.id.select_contact_button);
        mEmail = (Button) view.findViewById(R.id.send_email_button);

        //Set the widgets to the properties of the beverage
        mId.setText(mBeverage.getId());
        mId.setEnabled(false);
        mName.setText(mBeverage.getName());
        mPack.setText(mBeverage.getPack());
        mPrice.setText(Double.toString(mBeverage.getPrice()));
        mActive.setChecked(mBeverage.isActive());


        //Text changed listenter for the id. It will not be used since the id will be always be disabled.
        //It can be used later if we want to be able to edit the id.
        mId.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mBeverage.setId(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        //Text listener for the name. Updates the model as the name is changed
        mName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mBeverage.setName(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        //Text listener for the Pack. Updates the model as the text is changed
        mPack.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mBeverage.setPack(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        //Text listener for the price. Updates the model as the text is typed.
        mPrice.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //If the count of characters is greater than 0, we will update the model with the
                //parsed number that is input.
                if (count > 0) {
                    mBeverage.setPrice(Double.parseDouble(s.toString()));
                    //else there is no text in the box and therefore can't be parsed. Just set the price to zero.
                } else {
                    mBeverage.setPrice(0);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        //Set a checked changed listener on the checkbox
        mActive.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mBeverage.setActive(isChecked);
            }
        });

        mContactAddress = "";

        mContact.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                selectContact();
            }
        });

        mEmail.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                sendEmail();
            }
        });

        //Lastley return the view with all of this stuff attached and set on it.
        return view;
    }

    public void selectContact() {
        //Begin the implicit intent to select a contact to retrieve the email address from
        Intent i = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
        startActivityForResult(Intent.createChooser(i, "Select Contact"), REQUEST_CODE);
    }

    public void sendEmail(){
        //Begin the implicit intent to send an email
        Intent i = new Intent(Intent.ACTION_SEND);
        i.setType("text/html");

        //If a contact with an email address could be selected we prepopulate
        //the emails To, Subject, and Body fields with the information received from Contacts
        if(mContactAddress.length() > 0) {
            i.putExtra(Intent.EXTRA_EMAIL, new String[] { mContactAddress });
            i.putExtra(Intent.EXTRA_SUBJECT, mContactSubject);
            i.putExtra(Intent.EXTRA_TEXT, mContactBody);
        }
        startActivity(Intent.createChooser(i, "Send Email"));
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == Activity.RESULT_OK){
            switch(requestCode){
                case REQUEST_CODE:

                    //Declare the cursor with enough scope to close outside of the try
                    Cursor c = null;

                    try{

                        //Get the raw contact data from the intent
                        Uri uri = data.getData();
                        Log.v("CONTACT_DEBUG_TAG","Got contact: " + uri.toString());

                        //Get the id of the contact
                        String id = uri.getLastPathSegment();
                        Log.v("CONTACT_DEBUG_TAG","Got id: " + id.toString());

                        //Establish the cursor, allowing it to access the information we need from
                        //the contact at id
                        c = getActivity().getContentResolver()
                                .query(Email.CONTENT_URI,
                                       null,
                                       Email.CONTACT_ID + " = ?",
                                       new String[]{id},
                                       null);

                        //Get the index of the data we are querying for
                        int nameIndex = c.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME),
                            emailIndex = c.getColumnIndex(Email.DATA);

                        if(c.moveToFirst()){

                            //Get the contacts name and email address and assign them to class level
                            //variables for later use
                            mContactName = c.getString(nameIndex);
                            mContactAddress = c.getString(emailIndex);
                            mContactSubject = "Beverage App Item Info";
                            mContactBody = mContactName + ",\n\n" +
                                    "Please Review the Following Beverage" + "\n\n" +
                                    mBeverage.getId() + "\n" +
                                    mBeverage.getName() + "\n" +
                                    mBeverage.getPack() + "\n" +
                                    mBeverage.getPrice() + "\n" +
                                    mBeverage.isActive();

                            Log.v("CONTACT_DEBUG_TAG", "Full email body: " + "\n" + mContactBody);

                        } else {

                        }

                    } catch(Exception e) {
                        Log.v("CONTACT_DEBUG_TAG","Exception was thrown: " +
                                e.getCause() + "\n" +
                                e.getMessage() + "\n" +
                                e.getStackTrace());
                    } finally {
                        //Always close the cursor
                        //c.close();
                        Log.v("CONTACT_DEBUG_TAG","Cursor c closed");
                    }
                    break;
            }
        }

    }

}
