package com.example.camerasample;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;

public class MainActivity extends ActionBarActivity {

	private static final String TAG = "CameraFragment";
	private static final int PICK_FROM_CAMERA = 1;
	Button click;
	ImageView image;
	public static final String GridViewDemo_ImagePath = Environment
			.getExternalStorageDirectory().getAbsolutePath() + "/GridView/";
	private SimpleDateFormat dateFormat = new SimpleDateFormat(
			"yyyy-MM-dd HH:mm:ss");

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		click = (Button) findViewById(R.id.click);
		image = (ImageView) findViewById(R.id.image);

		click.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// Intent cameraIntent = new Intent(
				// android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
				// startActivityForResult(cameraIntent, PICK_FROM_CAMERA);

				Intent previewDemo = new Intent(MainActivity.this,
						PreviewDemo.class);
				startActivity(previewDemo);

				// Intent intent = new
				// Intent("android.media.action.IMAGE_CAPTURE");
				// File file = new File(GridViewDemo_ImagePath + "image.jpg");
				// intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
				// startActivityForResult(intent, PICK_FROM_CAMERA);

			}
		});
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == PICK_FROM_CAMERA && resultCode == Activity.RESULT_OK) {

			// Bitmap photo = (Bitmap) data.getExtras().get("data");
			//
			// String imgcurTime = dateFormat.format(new Date());
			// File imageDirectory = new File(GridViewDemo_ImagePath);
			// imageDirectory.mkdirs();
			// String _path = GridViewDemo_ImagePath + imgcurTime+".jpg";
			//
			// Log.d("_path", "_path"+_path);
			// try {
			// FileOutputStream out = new FileOutputStream(_path);
			// photo.compress(Bitmap.CompressFormat.JPEG, 100, out);
			// out.close();
			// } catch (FileNotFoundException e) {
			// e.getMessage();
			// } catch (IOException e) {
			// e.printStackTrace();
			// }

			File fileTemp = new File(GridViewDemo_ImagePath + "image.jpg");
			Bitmap photo = decodeSampledBitmapFromFile(
					fileTemp.getAbsolutePath(), 480, 320);

			// Bitmap photo = (Bitmap) data.getExtras().get("data");
			image.setImageBitmap(photo);

			ByteArrayOutputStream stream = new ByteArrayOutputStream();
			photo.compress(Bitmap.CompressFormat.JPEG, 100, stream);
			byte[] image = stream.toByteArray();

			String filename = GridViewDemo_ImagePath
					+ dateFormat.format(new Date()) + ".jpg";
			FileOutputStream os = null;
			boolean success = true;

			File file = new File(filename);

			try {
				os = new FileOutputStream(file);
				os.write(image);
			} catch (Exception e) {
				Log.e(TAG, "Error writing to file " + filename, e);
				success = false;
			} finally {
				try {
					if (os != null)
						os.close();
				} catch (Exception e) {
					Log.e(TAG, "Error closing file " + filename, e);
					success = false;
				}
			}

			if (success) {
				Log.i(TAG, "Jpeg saved at " + filename);

				fileTemp.delete();

			}
		}
	}

	public static Bitmap decodeSampledBitmapFromFile(String path, int reqWidth,
			int reqHeight) { // BEST QUALITY MATCH

		// First decode with inJustDecodeBounds=true to check dimensions
		final BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(path, options);

		// Calculate inSampleSize, Raw height and width of image
		final int height = options.outHeight;
		final int width = options.outWidth;
		options.inPreferredConfig = Bitmap.Config.RGB_565;
		int inSampleSize = 1;

		if (height > reqHeight) {
			inSampleSize = Math.round((float) height / (float) reqHeight);
		}
		int expectedWidth = width / inSampleSize;

		if (expectedWidth > reqWidth) {
			// if(Math.round((float)width / (float)reqWidth) > inSampleSize) //
			// If bigger SampSize..
			inSampleSize = Math.round((float) width / (float) reqWidth);
		}

		options.inSampleSize = inSampleSize;

		// Decode bitmap with inSampleSize set
		options.inJustDecodeBounds = false;

		return BitmapFactory.decodeFile(path, options);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
