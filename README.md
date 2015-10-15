# ImgBinarize
Binarize an 8 bit grayscale image.

**JavaFX** application allows to open an image.
It is then binarized, converted into a 2 color (black and white) image.
Binarized image can be saved to disk.

This project depends on [Cluster](https://github.com/naraku9333/Cluster) which does the actual binarization.
Build Cluster into a jar archive and add to classpath when building ImgBinarize.

Test images are in the images/ directory.

**NOTE:** I am not the creator, owner or copyright holder of any images. They are for testing purposes only.

This is my first foray into the JavaFX world. This project was written for academic purposes.

#####TODO:
- Add checks to verify images are 8 bit, currently if they are not an exception is thrown by the system.
