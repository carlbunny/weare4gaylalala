package com.aeviou.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import android.content.res.AssetManager;

public class FileUtils {
	

	public static void MergeFile(String file1,String file2, String file3) throws IOException{
		InputStream inputStream1 = AeviouConstants.inputMethodService.getAssets()
									.open(file1, AssetManager.ACCESS_BUFFER) ;
		InputStream inputStream2 = null;
		if (file2 != null){
		inputStream2 = AeviouConstants.inputMethodService.getAssets()
						.open(file2, AssetManager.ACCESS_BUFFER) ;
		}
		
		File dir = AeviouConstants.inputMethodService.getFilesDir();

		File file = new File(dir.getPath() + "/" + file3);
		if(file.exists())
			file.delete();
		
		if(!file.createNewFile()){
			ALog.v("faile to move dict:"+file1);
			return;
		}
		int count = 0;
		FileOutputStream outputStream = new FileOutputStream(file);
		byte[] buffer = new byte[8 * 1024];
		while((count = inputStream1.read(buffer)) > 0){
			outputStream.write(buffer, 0, count) ;
		}
		inputStream1.close();
		
		if (file2 != null){
			while((count = inputStream2.read(buffer)) > 0){
				outputStream.write(buffer, 0, count) ;
			}
			inputStream2.close();
		}

		outputStream.close();
		
	}
}
