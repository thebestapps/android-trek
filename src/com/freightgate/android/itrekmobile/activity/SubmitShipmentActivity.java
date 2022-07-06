package com.freightgate.android.itrekmobile.activity;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.json.JSONException;
import org.json.JSONObject;

import com.freightgate.android.itrekmobile.data.DatabaseManager;
import com.freightgate.android.itrekmobile.data.PendingRequestsDAO;
import com.freightgate.android.itrekmobile.data.ShipmentDAO;
import com.freightgate.android.itrekmobile.model.Shipment;
import com.freightgate.android.itrekmobile.server.ServerException;
import com.freightgate.android.itrekmobile.server.ServerManager;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.MediaStore.Images;
import android.provider.MediaStore.Images.Media;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Base64;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class SubmitShipmentActivity extends Activity {

	private ArrayList<Shipment> shipmentList;
	private LinearLayout mContent;
	private Signature mSignature;
	private Bitmap mBitmap;
	private Spinner poSpinner;
	final int REQUEST_IMAGE_CAPTURE_DAMAGE = 111555;
	final int REQUEST_IMAGE_CAPTURE_PAID = 555111;
	private String takenDamagePicture = "";
	private String takenPaidPicture = "";

	private Context context;

	private boolean signatureTouched = false;

	View mView;

	private int ttl_pieces = 0;
	private int received_pieces = 0;
	private int refused_pieces = 0;
	private String exception_text = "";


	private String status_code = "";
	private String status_txt = "";
	private String status_reason_code = "";
	private String status_reason_txt = "";

	private DatabaseManager dbManager;
	private ServerManager sManager;

	@SuppressWarnings("unchecked")
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.submit_shipment);

		ActionBar ab = getActionBar();

		ColorDrawable colorDrawable = new ColorDrawable(getResources().getColor(R.color.MAIN_COLOR));
		ab.setBackgroundDrawable(colorDrawable);

		dbManager = new DatabaseManager(SubmitShipmentActivity.this);
		sManager = new ServerManager(this, dbManager);

		context = this;

		shipmentList = (ArrayList<Shipment>) getIntent().getSerializableExtra("Shipment");
		signatureTouched = false;
		//add back-button to icon
		getActionBar().setDisplayHomeAsUpEnabled(true);
		//change title name
		getActionBar().setTitle("DELIVERY RECEIPT");


		////////////// SIGNATURE ///////////////////////////////////////
		mContent = (LinearLayout) findViewById(R.id.signaturelayout);
		mSignature = new Signature(this, null);
		mSignature.setBackgroundColor(Color.WHITE);
		mContent.addView(mSignature, LayoutParams.MATCH_PARENT, 270);
		mView = mContent;
//        mView.setDrawingCacheEnabled(true);

		////////////// set data of shipment  //////////////////////////

		setFormData(shipmentList);

		////////////// set text change listener for pieces ///////////

		EditText et_pieces = (EditText)findViewById(R.id.et_submit_form_pieces);
		et_pieces.addTextChangedListener(new TextWatcher() {

			@Override
			public void afterTextChanged(Editable text) {
				if(!text.toString().isEmpty()){
					received_pieces = Integer.valueOf(text.toString());
					setSubmitBtnText();
				}
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {

			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {


			}

		});
		et_pieces.setOnFocusChangeListener(new OnFocusChangeListener() {
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if(!hasFocus){
					EditText et_pieces = (EditText)findViewById(R.id.et_submit_form_pieces);
					if(et_pieces.getText().toString().isEmpty()){
						showAlert("Received field can't be empty");
						received_pieces = 0;
						et_pieces.setText("0");
					}
				}
			}
		});

	}

	// /////////////////// MENU ////

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case android.R.id.home:
				// home/back pressed
				Builder builder = new Builder(this);
				builder.setTitle("Alert Dialog")
						.setIcon(android.R.drawable.ic_dialog_alert)
						.setMessage("Are you sure you want to go back ? \nYou will loose all your data !")
						.setPositiveButton(android.R.string.ok,
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface arg0, int arg1) {
										finish();
									}
								}).setNegativeButton(android.R.string.cancel,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface arg0, int arg1) {
							}
						});

				builder.show();

				break;

			default:
				break;
		}

		return true;
	}

	public void onBackPressed(){
		// do something here and don't write super.onBackPressed()

		Builder builder = new Builder(this);
		builder.setTitle("Alert Dialog")
				.setIcon(android.R.drawable.ic_dialog_alert)
				.setMessage("Are you sure you want to go back ? \nYou will loose all your data !")
				.setPositiveButton(android.R.string.ok,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogIn
                                                        rface arg0, int arg1) {
								finish();
							}
						}).setNegativeButton(android.R.string.cancel,
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface arg0, int arg1) {
					}
				});

		builder.show();

	}

	////////////////////// CLICK EVENTS /////////////////////////


	public void onButtonClick(final View view) {

		switch (view.getId()) {
			case R.id.btn_submit_form_take_damage_picture:

				Builder builder = new Builder(this);
				builder.setTitle("Alert Dialog")
						.setIcon(android.R.drawable.ic_dialog_alert)
						.setMessage("Do you want to take a picture of a damage? \n\n")
						.setPositiveButton("Take picture",
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface arg0, int arg1) {
										dispatchTakePictureIntent(REQUEST_IMAGE_CAPTURE_DAMAGE);
									}
								}).setNegativeButton(android.R.string.cancel,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface arg0, int arg1) {
							}
						})
						.setNegativeButton(android.R.string.cancel,
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface arg0, int arg1) {

									}
								}).setNegativeButton(android.R.string.cancel,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface arg0, int arg1) {
							}
						})
						.setNeutralButton("Clear",
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface arg0, int arg1) {

										Drawable hotocorssdrawable = getResources().getDrawable( R.drawable.photo );

										Button damage = (Button) findViewById(R.id.btn_submit_form_take_damage_picture);
										damage.setCompoundDrawablesWithIntrinsicBounds(null,null,hotocorssdrawable,null);
										damage.setText("damage picture");
										takenDamagePicture = "";
									}
								}).setNegativeButton(android.R.string.cancel,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface arg0, int arg1) {
							}
						});

				builder.show();


				break;
			case R.id.btn_submit_form_exception:

				openExceptionDialog(view);

				break;
			case R.id.btn_submit_form_take_paid_picture:

				Builder builderpaid = new Builder(this);
				builderpaid.setTitle("Alert Dialog")
						.setIcon(android.R.drawable.ic_dialog_alert)
						.setMessage("Do you want to take a picture of a check? \n\n")
						.setPositiveButton("Take picture",
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface arg0, int arg1) {
										dispatchTakePictureIntent(REQUEST_IMAGE_CAPTURE_PAID);
									}
								}).setNegativeButton(android.R.string.cancel,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface arg0, int arg1) {
							}
						})
						.setNegativeButton(android.R.string.cancel,
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface arg0, int arg1) {

									}
								}).setNegativeButton(android.R.string.cancel,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface arg0, int arg1) {
							}
						})
						.setNeutralButton("Clear",
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface arg0, int arg1) {

										Drawable hotocorssdrawable = getResources().getDrawable( R.drawable.photo );

										Button paid = (Button) findViewById(R.id.btn_submit_form_take_paid_picture);
										paid.setCompoundDrawablesWithIntrinsicBounds(null,null,hotocorssdrawable,null);
										paid.setText("paid picture");
										takenPaidPicture = "";

									}
								}).setNegativeButton(android.R.string.cancel,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface arg0, int arg1) {
							}
						});

				builderpaid.show();

				break;

			case R.id.btn_submit_form_signature_clear:
				mSignature.clear();
//			PendingRequestsDAO pendingDao = new PendingRequestsDAO(this);
//			
//			pendingDao.open();
//			List<String> bla = pendingDao.getAllRequests();
//			pendingDao.close();
				break;

			case R.id.btn_submit_form_submit:
				EditText et_print_name = (EditText) findViewById(R.id.et_submit_form_print_name);

				if(et_print_name.getText().toString().matches("") || !signatureTouched){
					showMessage("Please sign the form and enter a print name");
				}else{
					submitForm(view);
				}

				break;

		}

	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event)
	{
		if(keyCode == KeyEvent.KEYCODE_HOME)
		{
			Log.d("Test", "Home button pressed!");
		}
		return super.onKeyDown(keyCode, event);
	}


	/////////////////////////////////////

	public void submitForm(final View view){

		Date now = new Date();


		// create f
		StringBuffer submitreuqest = new StringBuffer();
		for (int i = 0; i < shipmentList.size(); i++) {


			// create f
			submitreuqest = null;
			submitreuqest = new StringBuffer();
			mView.setDrawingCacheEnabled(true);
			String signatureBase64 = mSignature.saveBase64(mView);
			submitreuqest.append("&entry_signature_pic="+signatureBase64);
			submitreuqest.append("&db_id=-1");
			submitreuqest.append("&saveAs=0");
			submitreuqest.append("&entry_misc_json="+getQuestionJSON());
			setSubmitBtnText();
			submitreuqest.append("&entry_status_code="+status_code);
			submitreuqest.append("&entry_status_txt="+status_txt);
			submitreuqest.append("&entry_status_reason_code="+status_reason_code);
			submitreuqest.append("&entry_status_remarks="+status_reason_txt + ((exception_text != "")? " --- " + exception_text:"")+" "+((takenDamagePicture != "")?" --- DAMAGED" :""));
			SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
			submitreuqest.append("&entry_status_date="+format1.format(now.getTime()));
			SimpleDateFormat format2 = new SimpleDateFormat("HH:mm:ss", Locale.US);
			submitreuqest.append("&entry_status_time="+format2.format(now.getTime()));
			submitreuqest.append("&entry_billno="+shipmentList.get(i).getBillno());

			submitreuqest.append("&entry_ttl_pieces="+received_pieces);
			EditText et_weight = (EditText) findViewById(R.id.et_submit_form_weight);
			submitreuqest.append("&entry_ttl_weight="+et_weight.getText().toString());
			submitreuqest.append("&entry_exception=");
			EditText et_print_name = (EditText) findViewById(R.id.et_submit_form_print_name);
			submitreuqest.append("&entry_signature="+et_print_name.getText().toString());
			submitreuqest.append("&entry_pay_status="+((takenPaidPicture != "")? "PAID" : ""));

			//// write request to offline table
			PendingRequestsDAO pendingDao = new PendingRequestsDAO(context);
			pendingDao.open();
			pendingDao.createPendingRequest(shipmentList.get(i).getAgent_ref(),shipmentList.get(i).getBillno(),submitreuqest.toString());
			pendingDao.close();
			//// delete items from database
			ShipmentDAO shipDao = new ShipmentDAO(context);
			shipDao.open();
			shipDao.deleteShipment(shipmentList.get(i).getAgent_ref(), shipmentList.get(i).getBillno());
			shipDao.close();

		}




		// create paid request
		if(takenPaidPicture != ""){
			submitreuqest.append("&entry_paid_pic="+takenPaidPicture);
			PendingRequestsDAO pendingDao = new PendingRequestsDAO(context);
			pendingDao.open();
			pendingDao.createPendingRequest(shipmentList.get(0).getAgent_ref(),shipmentList.get(0).getBillno(),submitreuqest.toString());
			pendingDao.close();
		}

		// create damage request
		if(takenDamagePicture != ""){
			submitreuqest.append("&entry_damage_pic="+takenDamagePicture);
			PendingRequestsDAO pendingDao = new PendingRequestsDAO(context);
			pendingDao.open();
			pendingDao.createPendingRequest(shipmentList.get(0).getAgent_ref(),shipmentList.get(0).getBillno(),submitreuqest.toString());
			pendingDao.close();
		}




		/////////// try to send it /////////////////////////
		try {
			sManager.syncOpenRequests();

		} catch (ServerException e) {
			// TODO Auto-generated catch block
			Log.e("ServerException", e.getMessage());

			showMessage(e.getMessage());
			super.onBackPressed();

		}

//		super.onBackPressed();


	}


	private String getQuestionJSON() {
		CheckBox chk_q1 = (CheckBox) findViewById(R.id.chk_submit_form_q1);
		CheckBox chk_q2 = (CheckBox) findViewById(R.id.chk_submit_form_q2);
		CheckBox chk_q3 = (CheckBox) findViewById(R.id.chk_submit_form_q3);
		JSONObject array = new JSONObject();
		try {
			array.put("q1", chk_q1.isChecked());
			array.put("q2", chk_q2.isChecked());
			array.put("q3", chk_q3.isChecked());
		} catch (JSONException e) {
			e.printStackTrace();
		}


		return array.toString();
	}

	public void setFormData(ArrayList<Shipment> shipments){

		if(shipments.size() > 0){

			setPROSpinner(shipments);

			///// set Shipper
			TextView txt_shipper = (TextView) findViewById(R.id.txt_submit_form_shipper);
			String shipper = "SHIPPER: "+shipments.get(0).getShp_name()+"\n";
			shipper += shipments.get(0).getShp_add1()+"\n";
			shipper += shipments.get(0).getShp_add2()+"\n";
			shipper += shipments.get(0).getShp_city()+", "+ shipments.get(0).getShp_state()+", "+ shipments.get(0).getShp_postal()+"\n";
			shipper += shipments.get(0).getShp_phone()+"\n";
			shipper += "\n";
			shipper += "SHP REF#: "+(shipments.get(0).getShp_ref().trim().length()>0?shipments.get(0).getShp_ref().substring(0, 100):"");
			txt_shipper.setText(shipper);


			///// set consignee
			if(shipments.get(0).getAgent_num().isEmpty()){

				TextView txt_consignee = (TextView) findViewById(R.id.txt_submit_form_consignee);
				String consignee = "Consignee: "+shipments.get(0).getCsg_name()+"\n";
				consignee += shipments.get(0).getCsg_add1()+"\n";
				consignee += shipments.get(0).getCsg_add2()+"\n";
				consignee += shipments.get(0).getCsg_city()+", "+ shipments.get(0).getCsg_state()+", "+ shipments.get(0).getCsg_postal()+"\n";
				consignee += shipments.get(0).getCsg_phone()+"\n";
				consignee += "\n";
				consignee += "PO#: "+(shipments.get(0).getCsg_ref().trim().length()>0?shipments.get(0).getCsg_ref().substring(0, 100):"");
				txt_consignee.setText(consignee);

			}else{
				TextView txt_consignee = (TextView) findViewById(R.id.txt_submit_form_consignee);
				String agent = "Agent: "+shipments.get(0).getAgent_name()+"\n";
				agent += shipments.get(0).getAgent_add1()+"\n";
				agent += shipments.get(0).getAgent_add2()+"\n";
				agent += shipments.get(0).getAgent_city()+", "+ shipments.get(0).getAgent_state()+", "+ shipments.get(0).getAgent_postal()+"\n";
				agent += shipments.get(0).getAgent_phone()+"\n";
				agent += "\n";
				txt_consignee.setText(agent);

			}

			///// Deliver Date noew()
			TextView deliver_date = (TextView) findViewById(R.id.txt_submit_form_deliver_date_value);
			DateFormat df = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss", Locale.US);
			Date today = Calendar.getInstance().getTime();
			String reportDate = df.format(today);
			deliver_date.setText(reportDate);

			///// total pieces.
			EditText et_pieces = (EditText) findViewById(R.id.et_submit_form_pieces);
			TextView txt_total_pieces = (TextView) findViewById(R.id.txt_submit_form_total_pieces);
			int totalpieces = 0;
			for (int i = 0; i < shipments.size(); i++) {
				String pieces = (shipments.get(i).getTtl_pieces() != null)? shipments.get(i).getTtl_pieces() : "0";
				totalpieces += Integer.valueOf(pieces);
			}
			this.ttl_pieces = totalpieces;
			this.received_pieces = totalpieces;
			et_pieces.setText(String.valueOf(totalpieces));
			txt_total_pieces.setText("/"+String.valueOf(totalpieces));

			///// total weight.
			EditText total_weight = (EditText) findViewById(R.id.et_submit_form_weight);
			int totalweight = 0;
			for (int i = 0; i < shipments.size(); i++) {
				String weight = (shipments.get(i).getTtl_weight() != null)? shipments.get(i).getTtl_weight() : "0";
				totalweight += Integer.valueOf(weight);
			}
			total_weight.setText(String.valueOf(totalweight));

			// diesable take picture button damage and paid
			if(shipments.size() > 1){
				Drawable hotocorssdrawable = getResources().getDrawable( R.drawable.photo_cross );

				Button damage = (Button) findViewById(R.id.btn_submit_form_take_damage_picture);
				damage.setClickable(false);
				damage.setCompoundDrawablesWithIntrinsicBounds(null,null,hotocorssdrawable,null);

				Button paid = (Button) findViewById(R.id.btn_submit_form_take_paid_picture);
				paid.setClickable(false);
				paid.setCompoundDrawablesWithIntrinsicBounds(null,null,hotocorssdrawable,null);

			}

			// set print name
			EditText print_name = (EditText) findViewById(R.id.et_submit_form_print_name);
			print_name.setText(shipments.get(0).getSignature());

		}

	}

	public void setPROSpinner(ArrayList<Shipment> shipments) {

		poSpinner = (Spinner) findViewById(R.id.sp_submit_form_po_number);
		List<String> list = new ArrayList<String>();
		for (int i = 0; i < shipmentList.size(); i++) {
			list.add(shipmentList.get(i).getBillno());
		}
		ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, list);
		dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		poSpinner.setAdapter(dataAdapter);

	}

	public void openExceptionDialog(View v) {

		final View inflatedView = getLayoutInflater().inflate(R.layout.exception_dialog, null);
		LinearLayout layoutColor = (LinearLayout) inflatedView.findViewById(R.id.login_dialog);
		layoutColor.setBackgroundColor(getResources().getColor(R.color.WHITE));
		layoutColor.setMinimumHeight(160);

		EditText edit_refused_count =(EditText)inflatedView.findViewById(R.id.et_exception_dialog_refused_count);
		edit_refused_count.setText(String.valueOf(this.refused_pieces));

		EditText edit_exception_text =(EditText)inflatedView.findViewById(R.id.et_exception_dialog_exception_text);
		edit_exception_text.setText(String.valueOf(this.exception_text));

		Builder builder = new Builder(this);

		builder.setTitle(R.string.app_name)
				.setIcon(android.R.drawable.ic_dialog_info)
				.setPositiveButton(android.R.string.ok,
						new DialogInterface.OnClickListener() {

							public void onClick(DialogInterface arg0, int arg1) {

								EditText edit_exception_text =(EditText) inflatedView.findViewById(R.id.et_exception_dialog_exception_text);
								exception_text = edit_exception_text.getText().toString();


								EditText edit_refused_count =(EditText) inflatedView.findViewById(R.id.et_exception_dialog_refused_count);
								String tmp = edit_refused_count.getText().toString();
								if(tmp.isEmpty()){
									refused_pieces = 0;
									setSubmitBtnText();
									showAlert("Refused# can't be empty");
								}else{
									refused_pieces = Integer.parseInt(edit_refused_count.getText().toString());
									received_pieces = received_pieces - refused_pieces;
									EditText et_pieces =(EditText) findViewById(R.id.et_submit_form_pieces);
									et_pieces.setText(String.valueOf(received_pieces));
									setSubmitBtnText();
								}
							}
						})
				.setNegativeButton(android.R.string.cancel,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface arg0, int arg1) {

								showMessage("cancel clicked");
							}
						})
				.setView(inflatedView)
				.setCancelable(false);

		builder.show();

	}

	private void dispatchTakePictureIntent(int intenCode) {

		PackageManager packageManager = this.getPackageManager();
		// if device support camera?
		if (packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA)) {
			//yes
//			Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//		    if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
//		        startActivityForResult(takePictureIntent, intenCode);
//		    }
			ContentValues values = new ContentValues ();

			values.put (Media.IS_PRIVATE, 1);
			values.put (Media.TITLE, "Xenios Mobile Private Image");
			values.put (Media.DESCRIPTION, "Classification Picture taken via Xenios Mobile.");

			Uri picUri = getContentResolver ().insert (Media.EXTERNAL_CONTENT_URI, values);

			//Keep a reference in app for now, we might need it later.
			((ItrekMobileApplication) getApplication()).setCamPicUri (picUri);

			Intent takePicture = new Intent (MediaStore.ACTION_IMAGE_CAPTURE);

			//May or may not be populated depending on devices.
			takePicture.putExtra (MediaStore.EXTRA_OUTPUT, picUri);

			startActivityForResult (takePicture, intenCode);



		}else{
			//no
			showAlert("Mo camera available");
			Log.i("camera", "This device has no camera!");
		}

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {


		if (requestCode == REQUEST_IMAGE_CAPTURE_DAMAGE && resultCode == RESULT_OK) {

			Bitmap pic = null;
			Uri picUri = null;

			//Some Droid devices (as mine: Acer 500 tablet) leave data Intent null.
			if (data == null) {
				picUri = ((ItrekMobileApplication) getApplication ()).getCamPicUri ();
			} else
			{
				Bundle extras = data.getExtras ();
				picUri = (Uri) extras.get (MediaStore.EXTRA_OUTPUT);
			}

			try
			{
				pic = Media.getBitmap (getContentResolver (), picUri);
			} catch (FileNotFoundException ex)
			{
				Logger.getLogger (getClass ().getName ()).log (Level.SEVERE, null, ex);
			} catch (IOException ex)
			{
				Logger.getLogger (getClass ().getName ()).log (Level.SEVERE, null, ex);
			}


			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			int width = pic.getWidth();
			int height = pic.getHeight();
			pic = Bitmap.createScaledBitmap(pic, (width *2) /7, (height*2) /7, true);
			pic.compress(Bitmap.CompressFormat.JPEG, 90, baos); //bm is the bitmap object
			byte[] image = baos.toByteArray();
			String encodedImage = Base64.encodeToString(image, Base64.URL_SAFE);
			Log.d("LOOK", encodedImage);
			takenDamagePicture = encodedImage;

			Drawable hotocorssdrawable = getResources().getDrawable( R.drawable.photo_accept);
			Button damage = (Button) findViewById(R.id.btn_submit_form_take_damage_picture);
			damage.setCompoundDrawablesWithIntrinsicBounds(null,null,hotocorssdrawable,null);
			damage.setText("damage picture taken");

		}else if (requestCode == REQUEST_IMAGE_CAPTURE_PAID && resultCode == RESULT_OK) {


			Bitmap pic = null;
			Uri picUri = null;

			//Some Droid devices (as mine: Acer 500 tablet) leave data Intent null.
			if (data == null) {
				picUri = ((ItrekMobileApplication) getApplication ()).getCamPicUri ();
			} else
			{
				Bundle extras = data.getExtras ();
				picUri = (Uri) extras.get (MediaStore.EXTRA_OUTPUT);
			}

			try
			{
				pic = Media.getBitmap (getContentResolver (), picUri);
			} catch (FileNotFoundException ex)
			{
				Logger.getLogger (getClass ().getName ()).log (Level.SEVERE, null, ex);
			} catch (IOException ex)
			{
				Logger.getLogger (getClass ().getName ()).log (Level.SEVERE, null, ex);
			}


			//Don't keep capture in public storage space (no Android Gallery use)

			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			int width = pic.getWidth();
			int height = pic.getHeight();
			pic = Bitmap.createScaledBitmap(pic, (width*2) /7, (height*2)/7, true);
			pic.compress(Bitmap.CompressFormat.JPEG, 90, baos); //bm is the bitmap object
			byte[] image = baos.toByteArray();
			String encodedImage = Base64.encodeToString(image, Base64.URL_SAFE);
			Log.d("LOOK", encodedImage);
			takenPaidPicture = encodedImage;

			Drawable hotocorssdrawable = getResources().getDrawable( R.drawable.photo_accept);
			Button paid = (Button) findViewById(R.id.btn_submit_form_take_paid_picture);
			paid.setCompoundDrawablesWithIntrinsicBounds(null,null,hotocorssdrawable,null);
			paid.setText("paid picture taken");
		}
	}
	//////////////////////////////////////////

	private void setSubmitBtnText(){

		Button btn = (Button) findViewById(R.id.btn_submit_form_submit);

		if(shipmentList.get(0).getAgent_num().isEmpty()){
			status_code = "D";
			status_txt = "Delivered";
		}else{
			status_code = "AU";
			status_txt = "Arrived FinalMile";
		}

		if(received_pieces == ttl_pieces){
			btn.setText("Submit");
			status_reason_code = "";
			status_reason_txt = "";
		}else if(received_pieces > ttl_pieces) {
			btn.setText("Submit - Over");
			status_reason_code = "BG";
			status_reason_txt = "Delivery Overage";
		}else if(received_pieces == 0) {
			btn.setText("Submit - Refused");
			status_code = "R";
			status_txt = "Refused";
			status_reason_code = "";
			status_reason_txt = "";
		}else if(received_pieces < ttl_pieces && refused_pieces == 0) {
			btn.setText("Submit - Short");
			status_reason_code = "S1";
			status_reason_txt = "Delivery Shortage";
		}else if(received_pieces < ttl_pieces && refused_pieces > 0) {
			btn.setText("Submit - Partially Refused");
			status_reason_code = "BS";
			status_reason_txt = "Partial Refusal by Customer";
		}
	}

	private void showMessage(String message) {
		Toast.makeText(SubmitShipmentActivity.this, message, Toast.LENGTH_LONG).show();
	}

	@SuppressWarnings("deprecation")
	private void showAlert(String message) {
		AlertDialog alertDialog = new AlertDialog.Builder(
				SubmitShipmentActivity.this).create();

		// Setting Dialog Title
		alertDialog.setTitle("Alert Dialog");
		// Setting Dialog Message
		alertDialog.setMessage(message);
		// Setting Icon to Dialog
		alertDialog.setIcon(android.R.drawable.ic_dialog_alert);
		// Setting OK Button
		alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
			public void onClick(final DialogInterface dialog, final int which) {


			}
		});

		// Showing Alert Message
		alertDialog.show();
	}


	public class Signature extends View
	{
		private static final float STROKE_WIDTH = 5f;
		private static final float HALF_STROKE_WIDTH = STROKE_WIDTH / 2;
		private Paint paint = new Paint();
		private Path path = new Path();

		private float lastTouchX;
		private float lastTouchY;
		private final RectF dirtyRect = new RectF();

		public Signature(Context context, AttributeSet attrs)
		{
			super(context, attrs);
			paint.setAntiAlias(true);
			paint.setColor(Color.BLACK);
			paint.setStyle(Paint.Style.STROKE);
			paint.setStrokeJoin(Paint.Join.ROUND);
			paint.setStrokeWidth(STROKE_WIDTH);
		}

		public void save(View v)
		{
			Log.v("log_tag", "Width: " + v.getWidth());
			Log.v("log_tag", "Height: " + v.getHeight());

			if(mBitmap == null)
			{
				mBitmap =  Bitmap.createBitmap (mContent.getWidth(), mContent.getHeight(), Bitmap.Config.RGB_565);;
			}
			Canvas canvas = new Canvas(mBitmap);
			try
			{
				FileOutputStream mFileOutStream = new FileOutputStream("data/bla/");

				v.draw(canvas);
				mBitmap.compress(Bitmap.CompressFormat.PNG, 90, mFileOutStream);
				mFileOutStream.flush();
				mFileOutStream.close();
				String url = Images.Media.insertImage(getContentResolver(), mBitmap, "title", null);
				Log.v("log_tag","url: " + url);
				//In case you want to delete the file
				//boolean deleted = mypath.delete();
				//Log.v("log_tag","deleted: " + mypath.toString() + deleted);
				//If you want to convert the image to string use base64 converter

			}
			catch(Exception e)
			{
				Log.v("log_tag", e.toString());
			}
		}

		public String saveBase64(View v)
		{
			Log.v("log_tag", "Width: " + v.getWidth());
			Log.v("log_tag", "Height: " + v.getHeight());


			mBitmap = v.getDrawingCache();


			ByteArrayOutputStream baos=new  ByteArrayOutputStream();
			mBitmap.compress(Bitmap.CompressFormat.JPEG,35, baos);
			byte [] arr=baos.toByteArray();
			String result=Base64.encodeToString(arr, Base64.URL_SAFE);
			System.out.println(result);


//            showMessage(result);
//            return "data:image/png;base64,"+result;
			return result;


		}

		public void clear()
		{
			signatureTouched = false;

			path.reset();
			mView.setDrawingCacheEnabled(false);
			mView.setDrawingCacheEnabled(false);
			invalidate();
			mView.setDrawingCacheEnabled(true);
			setDrawingCacheEnabled(true);
		}

		@Override
		protected void onDraw(Canvas canvas)
		{
			canvas.drawPath(path, paint);
		}

		@Override
		public boolean onTouchEvent(MotionEvent event)
		{
			float eventX = event.getX();
			float eventY = event.getY();

			signatureTouched = true;

			switch (event.getAction())
			{
				case MotionEvent.ACTION_DOWN:
					path.moveTo(eventX, eventY);
					lastTouchX = eventX;
					lastTouchY = eventY;
					return true;

				case MotionEvent.ACTION_MOVE:

				case MotionEvent.ACTION_UP:

					resetDirtyRect(eventX, eventY);
					int historySize = event.getHistorySize();
					for (int i = 0; i < historySize; i++)
					{
						float historicalX = event.getHistoricalX(i);
						float historicalY = event.getHistoricalY(i);
						expandDirtyRect(historicalX, historicalY);
						path.lineTo(historicalX, historicalY);
					}
					path.lineTo(eventX, eventY);
					break;

				default:
					debug("Ignored touch event: " + event.toString());
					return false;
			}

			invalidate((int) (dirtyRect.left - HALF_STROKE_WIDTH),
					(int) (dirtyRect.top - HALF_STROKE_WIDTH),
					(int) (dirtyRect.right + HALF_STROKE_WIDTH),
					(int) (dirtyRect.bottom + HALF_STROKE_WIDTH));

			lastTouchX = eventX;
			lastTouchY = eventY;

			return true;
		}

		private void debug(String string){
		}

		private void expandDirtyRect(float historicalX, float historicalY)
		{
			if (historicalX < dirtyRect.left)
			{
				dirtyRect.left = historicalX;
			}
			else if (historicalX > dirtyRect.right)
			{
				dirtyRect.right = historicalX;
			}

			if (historicalY < dirtyRect.top)
			{
				dirtyRect.top = historicalY;
			}
			else if (historicalY > dirtyRect.bottom)
			{
				dirtyRect.bottom = historicalY;
			}
		}

		private void resetDirtyRect(float eventX, float eventY)
		{
			dirtyRect.left = Math.min(lastTouchX, eventX);
			dirtyRect.right = Math.max(lastTouchX, eventX);
			dirtyRect.top = Math.min(lastTouchY, eventY);
			dirtyRect.bottom = Math.max(lastTouchY, eventY);
		}
	}


	public void triggerBackButton(){
		super.onBackPressed();
	}

}
