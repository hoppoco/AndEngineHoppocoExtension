package dev.hoppoco.andengine.opengl.shader;

import org.andengine.opengl.shader.ShaderProgram;
import org.andengine.opengl.shader.constants.ShaderProgramConstants;
import org.andengine.opengl.shader.exception.ShaderProgramLinkException;
import org.andengine.opengl.util.GLState;
import org.andengine.opengl.vbo.attribute.VertexBufferObjectAttributes;

import android.opengl.GLES20;

/**
 * (c) Zynga 2011
 *
 * @author Nicolas Gramlich <ngramlich@zynga.com>
 * @since 13:56:44 - 25.08.2011
 */
public class PixelToneShaderProgram extends ShaderProgram {
	// ===========================================================
	// Constants
	// ===========================================================
	public static int sUniformModelViewPositionMatrixLocation = ShaderProgramConstants.LOCATION_INVALID;
	public static int sUniformTexture0Location = ShaderProgramConstants.LOCATION_INVALID;
	public static String sUniformTextureSize = "texture_size";
	public static int sUniformTextureSizeLocation = ShaderProgramConstants.LOCATION_INVALID;


	private static PixelToneShaderProgram INSTANCE;

	public static final String VERTEXSHADER =
			"uniform mat4 " + ShaderProgramConstants.UNIFORM_MODELVIEWPROJECTIONMATRIX + ";\n" +
			"attribute vec4 " + ShaderProgramConstants.ATTRIBUTE_POSITION + ";\n" +
			"attribute vec4 " + ShaderProgramConstants.ATTRIBUTE_COLOR + ";\n" +
			"attribute vec2 " + ShaderProgramConstants.ATTRIBUTE_TEXTURECOORDINATES + ";\n" +
			"varying vec4 " + ShaderProgramConstants.VARYING_COLOR + ";\n" +
			"varying vec2 " + ShaderProgramConstants.VARYING_TEXTURECOORDINATES + ";\n" +
			"void main() {\n" +
			"	" + ShaderProgramConstants.VARYING_COLOR + " = " + ShaderProgramConstants.ATTRIBUTE_COLOR + ";\n" +
			"	" + ShaderProgramConstants.VARYING_TEXTURECOORDINATES + " = " + ShaderProgramConstants.ATTRIBUTE_TEXTURECOORDINATES + ";\n" +
			"	gl_Position = " + ShaderProgramConstants.UNIFORM_MODELVIEWPROJECTIONMATRIX + " * " + ShaderProgramConstants.ATTRIBUTE_POSITION + ";\n" +
			"}";

	public static final String FRAGMENTSHADER =
			"precision lowp float;\n" +
			"uniform sampler2D " + ShaderProgramConstants.UNIFORM_TEXTURE_0 + ";\n" +
			"uniform vec2 " + sUniformTextureSize + ";\n" +
			"varying lowp vec4 " + ShaderProgramConstants.VARYING_COLOR + ";\n" +
			"varying mediump vec2 " + ShaderProgramConstants.VARYING_TEXTURECOORDINATES + ";\n" +
			"float get_color( vec2 v, float alpha){ \n"+
			"	float local_x = mod(v.x * "+ sUniformTextureSize + ".x, 4.0);\n"+
			"	float local_x0 = floor(mod(local_x, 2.0));\n"+
			"	float local_x1 = floor(local_x/2.0);\n"+

			"	float local_y = mod(v.y * "+ sUniformTextureSize + ".y, 4.0);\n"+
			"	float local_y0 = floor(mod(local_y, 2.0));\n"+
			"	float local_y1 = floor(local_y/2.0);\n"+

			"	float z = local_y1  +  mod(local_x1+local_y1,2.0) * 2.0 + local_y0 * 4.0 + mod(local_x0+local_y0,2.0) * 8.0;\n"+

			"	float local_a = alpha * 16.0;\n"+
			"	if(local_a > z) {\n"+
			"		return 1.0;\n"+
			"	};\n"+
			"	return 0.0;\n"+
			"}\n"+

			"void main() {\n" +
			"	gl_FragColor = vec4(" + ShaderProgramConstants.VARYING_COLOR + ".rgb,1.0)"+
			"	* texture2D(" + ShaderProgramConstants.UNIFORM_TEXTURE_0 + ", " + ShaderProgramConstants.VARYING_TEXTURECOORDINATES + ")"+
			"	* vec4(1.0,1.0,1.0,get_color("+ ShaderProgramConstants.VARYING_TEXTURECOORDINATES +","+ShaderProgramConstants.VARYING_COLOR+".a));\n"+
			"}";
	// ===========================================================
	// Fields
	// ===========================================================



	// ===========================================================
	// Constructors
	// ===========================================================

	private PixelToneShaderProgram() {
		super(PixelToneShaderProgram.VERTEXSHADER, PixelToneShaderProgram.FRAGMENTSHADER);
	}

	public static PixelToneShaderProgram getInstance() {
		if(PixelToneShaderProgram.INSTANCE == null) {
			PixelToneShaderProgram.INSTANCE = new PixelToneShaderProgram();
		}
		return PixelToneShaderProgram.INSTANCE;
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	@Override
	protected void link(final GLState pGLState) throws ShaderProgramLinkException {
		GLES20.glBindAttribLocation(this.mProgramID, ShaderProgramConstants.ATTRIBUTE_POSITION_LOCATION, ShaderProgramConstants.ATTRIBUTE_POSITION);
		GLES20.glBindAttribLocation(this.mProgramID, ShaderProgramConstants.ATTRIBUTE_COLOR_LOCATION, ShaderProgramConstants.ATTRIBUTE_COLOR);
		GLES20.glBindAttribLocation(this.mProgramID, ShaderProgramConstants.ATTRIBUTE_TEXTURECOORDINATES_LOCATION, ShaderProgramConstants.ATTRIBUTE_TEXTURECOORDINATES);

		super.link(pGLState);

		PixelToneShaderProgram.sUniformModelViewPositionMatrixLocation = this.getUniformLocation(ShaderProgramConstants.UNIFORM_MODELVIEWPROJECTIONMATRIX);
		PixelToneShaderProgram.sUniformTexture0Location = this.getUniformLocation(ShaderProgramConstants.UNIFORM_TEXTURE_0);
		PixelToneShaderProgram.sUniformTextureSizeLocation = this.getUniformLocation(PixelToneShaderProgram.sUniformTextureSize);
	}

	@Override
	public void bind(final GLState pGLState, final VertexBufferObjectAttributes pVertexBufferObjectAttributes) {
		super.bind(pGLState, pVertexBufferObjectAttributes);

		GLES20.glUniformMatrix4fv(PixelToneShaderProgram.sUniformModelViewPositionMatrixLocation, 1, false, pGLState.getModelViewProjectionGLMatrix(), 0);
		GLES20.glUniform1i(PixelToneShaderProgram.sUniformTexture0Location, 0);
	}

	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
