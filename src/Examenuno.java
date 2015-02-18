/**
 * Juego
 *
 * ????????????????????????????????????
 *
 * @author Joel Cantu a01035024
 * @version 8.0.2
 * @date 11/02/15
 */
 
import java.applet.Applet;
import java.applet.AudioClip;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Color;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.net.URL;
import java.util.LinkedList;

/**
 *
 * @author AntonioM
 */
public class Examenuno extends Applet implements Runnable, KeyListener{

    private final int iMAXANCHO = 10; // maximo numero de personajes por ancho
    private final int iMAXALTO = 8;  // maxuimo numero de personajes por alto
    private Base basPrincipal;         // Objeto principal
    private Base basMalo;         // Objeto malo
    private Base basFantasmita;
    
    /* objetos para manejar el buffer del Applet y este no parpadee */
    private Image    imaImagenApplet;   // Imagen a proyectar en Applet	
    private Graphics graGraficaApplet;  // Objeto grafico de la Imagen
    private AudioClip adcSonidoChimpy;   // Objeto sonido de Chimpy
    private int iVidas;
    private int iScore;
    private int iDireccion;
    private LinkedList<Base> lnkFantasmas;//varios fantasmas
    private int iVelocidad;
    private LinkedList<Base> lnkJuanito;
    private AudioClip adcSonidoChimpy2;   // Objeto sonido de Chimpy
    private int conJuanito;
    private int iVelJuan;
    private boolean pausa;
    private boolean esc;
    private Image GameOver;

    /** 
     * init
     * 
     * Metodo sobrescrito de la clase <code>Applet</code>.<P>
     * En este metodo se inizializan las variables o se crean los objetos
     * a usarse en el <code>Applet</code> y se definen funcionalidades.
     * 
     */
    public void init() {
        // hago el applet de un tamaño 500,500
        setSize(800,500);
        lnkFantasmas = new LinkedList<Base>();
        lnkJuanito = new LinkedList<Base>();
        conJuanito=0;
        pausa=true;
        esc=false;
        URL urlImagenGameOver = this.getClass().getResource("gameover.jpeg");
        GameOver = getImage(urlImagenGameOver);
        
        
        int iRandomJuanito = (int) (Math.random() *5)+10;
        for(int iI=1; iI<=iRandomJuanito; iI++){
            int iPosX = (int) (Math.random() * (getWidth()));    
            int iPosY = (int) (Math.random() *-1* (getHeight()/2)); 
            URL urlImagenPrincipal = this.getClass().getResource("juanito.gif");
                
        // se crea el objeto para principal 
            basPrincipal = new Base(iPosX, iPosY, getWidth() / iMAXANCHO,
                getHeight() / iMAXALTO,
                Toolkit.getDefaultToolkit().getImage(urlImagenPrincipal));

        // se posiciona a principal  en la esquina superior izquierda del Applet 
           
            lnkJuanito.add(basPrincipal);
         }
        // defino la imagen del malo
	URL urlImagenMalo = this.getClass().getResource("chimpy.gif");
        
        // se crea el objeto para malo 
        int iPosX = (iMAXANCHO - 1) * getWidth() / iMAXANCHO;
        int iPosY = (iMAXALTO - 1) * getHeight() / iMAXALTO;        
	basMalo = new Base(iPosX,iPosY, getWidth() / iMAXANCHO,
                getHeight() / iMAXALTO,
                Toolkit.getDefaultToolkit().getImage(urlImagenMalo));
        
        URL urlSonidoChimpy = this.getClass().getResource("monkey2.wav");
        adcSonidoChimpy = getAudioClip (urlSonidoChimpy);
        adcSonidoChimpy.play();
        
        URL urlSonidoChimpy2 = this.getClass().getResource("monkey1.wav");
        adcSonidoChimpy2 = getAudioClip (urlSonidoChimpy2);
        
        iVelJuan=1;
        iVelocidad=(int) (Math.random() * 3) + 3;
        iVidas = (int) (Math.random() * 3) + 3;
        iScore=0;
        iDireccion=0;
        addKeyListener(this);
        
        int iRandom = (int) (Math.random() *2)+8;
        for(int iI=1; iI<=iRandom; iI++)
        {
        // se posiciona fantasmita 
 	 iPosX = (int) (Math.random() *-1* (getWidth()/2));    
         iPosY = (int) (Math.random() * (getHeight()));    
	URL urlImagenFantasmita = this.getClass().getResource("fantasmita.gif");
        
        // se crea el objeto para fantasmita 
        
	basFantasmita = new Base(iPosX,iPosY,getWidth()/iMAXANCHO,getHeight()/iMAXALTO, 
                Toolkit.getDefaultToolkit().getImage(urlImagenFantasmita));
                lnkFantasmas.add(basFantasmita);
        
        }
    }
	
    /** 
     * start
     * 
     * Metodo sobrescrito de la clase <code>Applet</code>.<P>
     * En este metodo se crea e inicializa el hilo
     * para la animacion este metodo es llamado despues del init o 
     * cuando el usuario visita otra pagina y luego regresa a la pagina
     * en donde esta este <code>Applet</code>
     * 
     */
    public void start () {
        // Declaras un hilo
        Thread th = new Thread (this);
        // Empieza el hilo
        th.start ();
    }
	
    /** 
     * run
     * 
     * Metodo sobrescrito de la clase <code>Thread</code>.<P>
     * En este metodo se ejecuta el hilo, que contendrá las instrucciones
     * de nuestro juego.
     * 
     */
    public void run () {
        /* mientras dure el juego, se actualizan posiciones de jugadores
           se checa si hubo colisiones para desaparecer jugadores o corregir
           movimientos y se vuelve a pintar todo
        */ 
        while (iVidas!=0) {
            if(pausa && !esc){
            actualiza();
            checaColision();
            repaint();
            }
            try	{
                // El thread se duerme.
                Thread.sleep (20);
            }
            catch (InterruptedException iexError) {
                System.out.println("Hubo un error en el juego " + 
                        iexError.toString());
            }
	}
    }
	
    /** 
     * actualiza
     * 
     * Metodo que actualiza la posicion de los objetos 
     * 
     */
    public void actualiza(){
switch(iDireccion)
        {
            case 1:{
                {basMalo.setY(basMalo.getY() - 1 );
            }
                break;
            }
            case 2:{
                {basMalo.setY(basMalo.getY() + 1);
            }
              break; 
            }
            case 3:{
                {basMalo.setX(basMalo.getX() - 1);
            }
                break;
            }
            case 4:
            {
                {basMalo.setX(basMalo.getX() +1);
            }
                break;
            }
            
            
        }
      

         if (basMalo.getX() < 0) {
            basMalo.setX(0); 
        } 
             if (basMalo.getX() + basMalo.getAncho() > getWidth()) { 
            basMalo.setX(getWidth() - basMalo.getAncho());  
            
             }
             if (basMalo.getY() < 0) { 
            basMalo.setY(0);
             }
             if (basMalo.getY() + basMalo.getAlto() > getHeight()) {
            basMalo.setY(getHeight() - basMalo.getAlto());
            }
             
             for(Base basFantasmita:lnkFantasmas) {
               basFantasmita.setX(basFantasmita.getX() + iVelocidad);
              
                 if (basFantasmita.getX() + basFantasmita.getAncho() > getWidth()) { 
                basFantasmita.setX((int) (Math.random() *-1* getWidth()));     
                basFantasmita.setY((int) ((Math.random()) * getHeight()));
                
                }
                 
             }
              for(Base basPrincipal:lnkJuanito) {
               basPrincipal.setY(basPrincipal.getY() + iVelJuan);
              
                 if (basPrincipal.getY() + basPrincipal.getAlto() > getHeight()) { 
                basPrincipal.setX((int) (Math.random() * getWidth()));     
                basPrincipal.setY((int) ((Math.random()) *-1* getHeight()/2));
                
                }
                 
             }
              
              
    }
	
    /**
     * checaColision
     * 
     * Metodo usado para checar la colision entre objetos
     * 
     */
    public void checaColision(){
         for(Base basFantasmita:lnkFantasmas){
            if(basMalo.intersecta(basFantasmita)){
                iScore++;
                basFantasmita.setX((int) (Math.random() *-1* getWidth()));     
                basFantasmita.setY((int) ((Math.random()) * getHeight())); 
                adcSonidoChimpy.play();
            }
        }
         
         for(Base basPrincipal:lnkJuanito){
            if(basMalo.intersecta(basPrincipal)){
                
                basPrincipal.setX((int) (Math.random() * getWidth()));     
                basPrincipal.setY((int) ((Math.random()) *-1* getHeight()/2)); 
                adcSonidoChimpy2.play();
                conJuanito++;
                
                
                if(conJuanito==5){
                    iVidas--;
                    iVelJuan++;
                    conJuanito=0;
                }
                
            }
            
        }
         
         
    }
	
    /**
     * update
     * 
     * Metodo sobrescrito de la clase <code>Applet</code>,
     * heredado de la clase Container.<P>
     * En este metodo lo que hace es actualizar el contenedor y 
     * define cuando usar ahora el paint
     * 
     * @param graGrafico es el <code>objeto grafico</code> usado para dibujar.
     * 
     */
    public void update (Graphics graGrafico){
        // Inicializan el DoubleBuffer
        if (imaImagenApplet == null){
                imaImagenApplet = createImage (this.getSize().width, 
                        this.getSize().height);
                graGraficaApplet = imaImagenApplet.getGraphics ();
        }

        // Actualiza la imagen de fondo.
        URL urlImagenFondo = this.getClass().getResource("Ciudad.png");
        Image imaImagenFondo = Toolkit.getDefaultToolkit().getImage(urlImagenFondo);
         graGraficaApplet.drawImage(imaImagenFondo, 0, 0, getWidth(), getHeight(), this);

        // Actualiza el Foreground.
        graGraficaApplet.setColor (getForeground());
        paint(graGraficaApplet);

        // Dibuja la imagen actualizada
        graGrafico.drawImage (imaImagenApplet, 0, 0, this);
    }
    
    /**
     * paint
     * 
     * Metodo sobrescrito de la clase <code>Applet</code>,
     * heredado de la clase Container.<P>
     * En este metodo se dibuja la imagen con la posicion actualizada,
     * ademas que cuando la imagen es cargada te despliega una advertencia.
     * 
     * @param graDibujo es el objeto de <code>Graphics</code> usado para dibujar.
     * 
     */
    public void paint(Graphics graDibujo) {
        if(pausa && !esc){
        // si la imagen ya se cargo
        if (basPrincipal != null) {
                //Dibuja la imagen de principal en el Applet
                for(Base basPrincipal:lnkJuanito){
                basPrincipal.paint(graDibujo, this);
                }
                //Dibuja la imagen de malo en el Applet
                basMalo.paint(graDibujo, this);
                
                 for(Base basFantasmita:lnkFantasmas)
                 {
                     basFantasmita.paint(graDibujo, this);
                 }
                
                graDibujo.setColor(Color.red); //pone el string en color rojo
            graDibujo.drawString("Score= " + iScore, 50, 50); //Dibuja los puntos
            
              graDibujo.setColor(Color.red); //pone el string en color rojo
            graDibujo.drawString("Vidas= " + iVidas, 120, 50); //Dibuja los puntos
                
                
        } // sino se ha cargado se dibuja un mensaje 
        else {
                //Da un mensaje mientras se carga el dibujo	
                graDibujo.drawString("No se cargo la imagen..", 20, 20);
        }
         if (iVidas == 0) {                 // si se acaban las vidas
             graDibujo.drawString("GAME OVER", 350, 350);       // dibuja Game Over    
        }
         if(esc){
                          graDibujo.drawString("GAME OVER", 350, 350);       // dibuja Game Over    

         }
             
        }
        
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
          	if(e.getKeyCode() == KeyEvent.VK_W){//Presiono W
			iDireccion = 1;               //decrementa o incrementa el contador dependiendo del eje
                                        //cambia el eje opuesto a 0
		} if(e.getKeyCode() == KeyEvent.VK_S){    //Presiono S
			iDireccion = 2;
                       
		} if(e.getKeyCode() == KeyEvent.VK_A){    //Presiono A
			iDireccion = 3;
                        
		} if(e.getKeyCode() == KeyEvent.VK_D){    //Presiono D
			iDireccion = 4;
                        
                    }
                if(e.getKeyCode() == KeyEvent.VK_ESCAPE){
                    esc=true;
                    
                }
                
    }

    @Override
    public void keyReleased(KeyEvent e) {
        if(e.getKeyCode()== KeyEvent.VK_P){
                    if(pausa){
                    pausa=false;
                    }
                    else{pausa=true;}
                
                }
    }
    

    
}