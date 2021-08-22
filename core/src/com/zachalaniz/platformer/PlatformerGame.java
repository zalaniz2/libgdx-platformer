package com.zachalaniz.platformer;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;

public class PlatformerGame extends ApplicationAdapter {

	private Player player;
	private AssetManager assetManager;
	private SpriteBatch batch;
	private TiledMap map;
	private OrthogonalTiledMapRenderer renderer;
	private OrthographicCamera camera;
	private Texture playerTexture;
	private float deltaTime;
	private ShapeRenderer debugRenderer;
	private GameManager manager;
	private final float SMOOTHING = 0.2f; // lower the smoother
	private Vector3 cameraPosition;


	@Override
	public void create () {
		assetManager = new AssetManager();
		batch = new SpriteBatch();
		assetManager.setLoader(TiledMap.class, new TmxMapLoader(new InternalFileHandleResolver()));
		assetManager.load("level.tmx", TiledMap.class);
		assetManager.load("player.png", Texture.class);
		assetManager.finishLoading();

		map = assetManager.get("level.tmx");
		playerTexture = assetManager.get("player.png");

		player = new Player(playerTexture);

		renderer = new OrthogonalTiledMapRenderer(map, 1 / 32f);
		camera = new OrthographicCamera();
		camera.setToOrtho(false, 15, 10);
		camera.update();

		player.position.set(3, 5);
		debugRenderer = new ShapeRenderer();
		manager = new GameManager(map);
		Gdx.gl.glClearColor( 3/255f, 198/255f, 252/255f, 1 );
		cameraPosition = new Vector3();
	}

	@Override
	public void render () {
		Gdx.gl.glClear( GL20.GL_COLOR_BUFFER_BIT );
		deltaTime = Gdx.graphics.getDeltaTime();
		cameraPosition.set(player.position.x, player.position.y, 0f);
		camera.position.lerp(cameraPosition, SMOOTHING);
		camera.position.x = MathUtils.clamp(camera.position.x, camera.viewportWidth/2, 30 - camera.viewportWidth/2);
		camera.position.y = MathUtils.clamp(camera.position.y, camera.viewportHeight/2, 20 - camera.viewportHeight/2);
		camera.update();
		renderer.setView(camera);
		manager.step(player, deltaTime);
		renderer.render();
		batch.setProjectionMatrix(camera.combined);
		batch.begin();
		batch.draw(player.playerTexture, player.position.x, player.position.y, player.WIDTH, player.HEIGHT);
		batch.end();
		//renderDebug();
	}
	
	@Override
	public void dispose () {
		assetManager.dispose();
	}

	private void renderDebug () {
		debugRenderer.setProjectionMatrix(camera.combined);
		debugRenderer.begin(ShapeRenderer.ShapeType.Line);

		debugRenderer.setColor(Color.RED);
		debugRenderer.rect(player.position.x, player.position.y, player.WIDTH, player.HEIGHT);

		debugRenderer.setColor(Color.YELLOW);
		TiledMapTileLayer layer = (TiledMapTileLayer)map.getLayers().get("blocked");
		for (int y = 0; y <= layer.getHeight(); y++) {
			for (int x = 0; x <= layer.getWidth(); x++) {
				TiledMapTileLayer.Cell cell = layer.getCell(x, y);
				if (cell != null) {
					if (camera.frustum.boundsInFrustum(x + 0.5f, y + 0.5f, 0, 1, 1, 0))
						debugRenderer.rect(x, y, 1, 1);
				}
			}
		}
		debugRenderer.end();
	}
}
