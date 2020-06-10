package com.dub.spring.services;

import org.springframework.beans.factory.annotation.Value;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.dub.spring.entities.Photo;
import com.dub.spring.exceptions.NoUploadFileException;
import com.dub.spring.exceptions.PhotoNotFoundException;
import com.dub.spring.exceptions.PhotoUploadException;
import com.dub.spring.exceptions.UnauthorizedException;
import com.dub.spring.repositories.PhotoRepository;

@Service
public class PhotoServiceImpl implements PhotoService {
	
	@Value("${baseDirPath}")
	String baseDirPath;
		
	@Autowired 
	private PhotoRepository photoRepository;

	@Override
	public List<Photo> getPhotosForCurrentUser(String username) {
		List<Photo> photos = photoRepository.findPhotosByUsername(username);
		return photos;
	}

	@Override
	public List<Photo> getSharedPhotos() {
		List<Photo> photos = photoRepository.findPhotosByShared(true);
		return photos;
	}


	@Override
	public long createPhoto(
				MultipartFile uploadedFileRef, String username, 
				String title, boolean shared) throws IOException {			
		InputStream is = null;     
		OutputStream os = null;
	
		// This buffer will store the data read from 'uploadedFileRef'
		byte[] buffer = new byte[1000];
		int bytesRead = -1;
		int totalBytes = 0;
	    
		String fileName = "photo" + "Tmp.jpg";
	
		String path = baseDirPath + fileName;
	
		if (uploadedFileRef.getSize() == 0) {
			//logger.debug("throwing NoUploadFileException");
			throw new NoUploadFileException();
		}
	
		is = uploadedFileRef.getInputStream();
		os = new FileOutputStream(path);
	
		while ((bytesRead = is.read(buffer)) != -1) {
			os.write(buffer);
			totalBytes += bytesRead;
		}
		os.close();
		
		if (totalBytes != uploadedFileRef.getSize()) {
			//logger.debug("throwing");
			throw new PhotoUploadException();
		} else {
			// now update database
			Photo photo = new Photo();
			photo.setTitle(title);
			photo.setUsername(username);
			photo.setShared(shared);
			long newId = photoRepository.save(photo).getId();
			
			// now change file name
			Path source = FileSystems.getDefault().getPath(baseDirPath, fileName);
			Path target = FileSystems.getDefault().getPath(baseDirPath, "photo" + newId + ".jpg");
			//Path source = 
			Files.move(source, target);
				
			return newId;
		}	
	}


	@Override
	public Photo getPhoto(long id, String username) {
		
		Optional<Photo> photo = photoRepository.findById(id);// may be null	
		
		if (!photo.isPresent()) {
			throw new PhotoNotFoundException();
		} else {
		
			if ( username.equals(photo.get().getUsername()) ) {
					return photo.get();
			} else {
				throw new UnauthorizedException();
			}
		}
	}

	@Override
	public void updatePhoto(Photo photo, String username) {
		if (!username.equals(photo.getUsername())) {
			throw new UnauthorizedException();
		}
		photoRepository.save(photo);
		
	}

	private void deletePhoto(long id) throws IOException {
		// delete row in database
		String filename = "photo" + id + ".jpg";
				
		try {
			photoRepository.deleteById(id);
			// delete actual photo file			
			Path path = FileSystems.getDefault().getPath(baseDirPath, filename);
			
			Files.deleteIfExists(path);
		} catch (EmptyResultDataAccessException e) {
			throw new PhotoNotFoundException();
		} 
		
	}

	@Override
	public void deletePhoto(long id, String username) throws Exception {
		
		Optional<Photo> photo = photoRepository.findById(id);
		if (!photo.isPresent()) {
			throw new PhotoNotFoundException();
		} else {// photo is present
		
			String user;
		
			user = photo.get().getUsername();	
		
			if (!user.equals(username)) {
				//logger.debug("throwing UnauthorizedException");
				throw new UnauthorizedException();
			}
		
			deletePhoto(id);
		}
		
	}


	@Override
	public InputStream loadPhoto(long id, String username) throws FileNotFoundException {
		Optional<Photo> photo = photoRepository.findById(id);
		
		if (!photo.isPresent()) {
			throw new PhotoNotFoundException();
		} else {
			
			Photo ph = photo.get();
			String path = baseDirPath + "photo" + ph.getId() + ".jpg";
			
			if (ph.isShared() || username.equals(ph.getUsername())) {			
				return new FileInputStream(path);// throws FileNotFoundException
			} else {
				throw new UnauthorizedException();// caught by RestEndpoint
			}	
		}
	}

}
