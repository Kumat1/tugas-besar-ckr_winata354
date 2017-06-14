import java.applet.Applet;
import java.awt.*;

/**
 * Created by Ari on 6/13/2017.
 **/

public class Tembak extends Applet implements Runnable
{
    final int	rowsofenemies = 3; //lawan
    final int	enemiesperrow = 5;
    final int	maxbombs = 10;
    final int	enemyxdelta = 8;
    final int	enemyydelta = 6;
    final int	enemyxoffset = 80;
    final int	groundheight = 5;
    final int	rotsteps = 32;
    final int	turretmarge = 24;
    final int	bombspeed = 3;
    final Color turretcolor = new Color(0,200,200);
    final Color	enemycolor1 = new Color(255,255,0);
    final Color	enemycolor2 = new Color(255,200,20);
    final Color	enemycolor3 = new Color(255,150,40);
    final Color   enemycolor4 = new Color(255,100,60);
    final Color   enemycolor5 = new Color(255,50,80);
    final Color   enemycolor6 = new Color(255,0,100);
    final Color	bulletcolor = new Color (255,255,255);
    final Color	bombcolor = new Color(255,255,255);
    final Color	groundcolor = new Color(0,255,0);
    final Color	backgnd = new Color (0,0,32);
    final Color	textcolor1 = new Color(255,160,64);
    final Color	textcolor2 = new Color(96,128,255);
    final Color	textcolor3 = new Color(255,96,128);
    final double	pi=(double)3.141592653589793238462643383279502884197;
    Dimension	d;
    Font 		largefont = new Font("Helvetica", Font.BOLD, 24);
    Font		smallfont = new Font("Helvetica", Font.BOLD, 14);
    FontMetrics	fmsmall, fmlarge;
    Graphics	goff;
    Image		ii;
    Thread	thethread;
    Color		enemycolors[] = { enemycolor1, enemycolor2, enemycolor3,
            enemycolor4, enemycolor5, enemycolor6 };
    int[]		bombx, bomby;
    boolean[]	drawbomb;
    boolean	ingame=false;
    int		bulletx, bullety;
    int		enemyx, enemyy, turretx,enemydx,enemyspeed;
    boolean[]	drawenemy;
    int		enemyradius;
    int		score;
    int[]		rotx, roty;
    int		animpos, animdpos;
    int		enemycount, enemydelay;
    int		speckcount;
    int		bombcount,curmaxbombs;
    int		turretdx=0;
    int[]		polyx,polyy;
    int		lives=4;
    int		groundy = 210;
    boolean	dying;

    public String getAppletInfo()
    {
        return("Perang - by ariWinata");
    }


    public void init()
    {
        Graphics g;
        int i;

        d = size();
        g=getGraphics();
        g.setFont(smallfont);
        fmsmall = g.getFontMetrics();
        g.setFont(largefont);
        fmlarge = g.getFontMetrics();

        drawbomb = new boolean[maxbombs];
        bombx=new int[maxbombs];
        bomby=new int[maxbombs];
        drawenemy=new boolean[enemiesperrow*rowsofenemies];
        rotx=new int[rotsteps];
        roty=new int[rotsteps];
        polyx=new int[4];
        polyy=new int[4];
        groundy=d.height-30;

        enemyradius = (d.width-enemyxoffset)/enemiesperrow-enemyxdelta;
        for (i=0; i<rotsteps; i++)
        {
            rotx[i]=(int)(enemyradius/2 * Math.sin(2*pi*i/rotsteps));
            roty[i]=(int)(enemyradius/2 * Math.cos(2*pi*i/rotsteps));
        }
        GameInit();
        ingame=false;
    }


    public void GameInit()
    {
        score=0;
        LevelInit();
        ingame=true;
        curmaxbombs=4;
        lives=3; // nyawa
    }


    public void LevelInit()
    {
        int i;

        enemyx=enemyradius;
        enemyy=enemyradius;
        enemydx=1;
        animpos=0;
        animdpos=-1;
        enemycount=0;
        enemydelay=6;
        speckcount=enemiesperrow*rowsofenemies;
        for (i=0; i<enemiesperrow*rowsofenemies; i++)
            drawenemy[i]=true;
        enemyspeed=1;
        dying=false;
        LevelContinue();
    }


    public void LevelContinue()
    {
        int i;

        bulletx=-1;
        bullety=-1;
        for (i=0; i<maxbombs; i++)
            drawbomb[i]=false;
        bombcount=0;
        turretx=d.width/2;
    }


    public boolean keyDown(Event e, int key)
    {
        if (ingame)
        {
            switch(key)
            {
                case Event.LEFT:
                    turretdx=-2;
                    break;
                case Event.RIGHT:
                    turretdx=2;
                    break;
                case Event.ESCAPE:
                    ingame=false;
                    break;
            }
        }
        else
        {
            if (key=='s' || key=='S')
                GameInit();
        }
        return true;
    }


    public boolean keyUp(Event e, int key)
    {
        if (key==Event.LEFT || key==Event.RIGHT)
            turretdx=0;
        if (key==' ' && bullety<0)
        {
            bullety=groundy-7;
            bulletx=turretx;
        }
        return true;
    }


    public void DrawSpecks()
    {
        int i,j;
        int t1=(animpos+rotsteps/8)%rotsteps;
        int t3=(animpos+rotsteps/2)%rotsteps;
        int t2=(animpos+rotsteps/2+rotsteps/8)%rotsteps;

        int x, y=enemyy;
        int range = (int)(.4*enemyradius);

        for (i=0; i<rowsofenemies; i++)
        {
            goff.setColor(enemycolors[i]);
            x=enemyx;
            for (j=0; j<enemiesperrow; j++)
            {
                if (drawenemy[i*enemiesperrow+j])
                {
                    polyx[0]=x+rotx[animpos];
                    polyx[1]=x+rotx[t1];
                    polyx[2]=x+rotx[t2];
                    polyx[3]=x+rotx[t3];
                    polyy[0]=y+roty[animpos];
                    polyy[1]=y+roty[t1];
                    polyy[2]=y+roty[t2];
                    polyy[3]=y+roty[t3];

                    goff.drawPolygon(polyx,polyy,4);
                    if ( bulletx>(x-range) && bulletx<(x+range) && bullety>(y-range) && bullety<(y+range))
                    {
                        score+=(rowsofenemies-i)*10;
                        drawenemy[i*enemiesperrow+j]=false;
                        bullety=-1;
                    }
                }
                x+=enemyradius+enemyxdelta;
            }
            y+=enemyradius+enemyydelta;
        }
        animpos=animpos+animdpos;
        if (animpos<0)
            animpos=rotsteps-1;
        if (animpos>=rotsteps)
            animpos=0;
    }


    public void MoveSpecks()
    {
        int minx=30000, maxx=-1, maxy=-1;
        int i,j;
        int x, y=enemyy;

        speckcount=0;
        enemycount++;
        if (enemycount>=enemydelay)
        {
            enemyx+=enemydx*enemyspeed;
        }

        for (i=0; i<rowsofenemies; i++)
        {
            x=enemyx;
            for (j=0; j<enemiesperrow; j++)
            {
                if (drawenemy[i*enemiesperrow+j])
                {
                    speckcount++;
                    if (x<minx) minx=x;
                    if (x>maxx) maxx=x;
                    if (y>maxy) maxy=y;
                }
                x+=enemyradius+enemyxdelta;
            }
            y+=enemyradius+enemyydelta;
        }


        if (enemycount>=enemydelay)
        {
            enemycount=0;

            if (minx<=enemyradius || maxx>=(d.width-enemyradius))
            {
                enemyy+=enemyydelta;
                enemydx=-enemydx;
                animdpos=-animdpos;
            }
        }

        if (maxy>=(groundy-enemyradius))
            ingame=false;
        if (speckcount<=32)
            enemydelay=5;
        if (speckcount<=16)
            enemydelay=4;
        if (speckcount<=8)
            enemydelay=3;
        if (speckcount<=4)
            enemydelay=2;
        if (speckcount<=2)
            enemydelay=1;
        if (speckcount==1)
            enemyspeed=4;
        if (speckcount==0)
        {
            curmaxbombs++;
            if (curmaxbombs>=maxbombs)
                curmaxbombs=maxbombs;
            LevelInit();
        }
    }


    public void DoTurret()
    {
        int i;
        int y=groundy-1;

        if (!dying)
        {
            turretx+=turretdx;
            if (turretx>=(d.width-turretmarge))
                turretx=d.width-turretmarge;
            if (turretx<=turretmarge)
                turretx=turretmarge;
        }
        else
        {
            turretx+=6;
            if (turretx>=d.width)
            {
                dying=false;
                lives--;
                if (lives<=0)
                    ingame=false;
                else
                    LevelContinue();
            }
        }

        goff.setColor(turretcolor);
        for (i=6; i>=0; i--)
        {
            goff.drawLine(turretx-i,y,turretx+i,y);
            y--;
        }
    }


    public void DoBullet()
    {
        if (bullety>=0)
        {
            bullety-=10;
            goff.setColor(bulletcolor);
            goff.drawLine(bulletx,bullety-1,bulletx,bullety+1);
        }
    }


    public void DoBombs()
    {
        int i;

        goff.setColor(bombcolor);
        for (i=0; i<curmaxbombs; i++)
        {
            if (drawbomb[i])
            {
                goff.fillRect(bombx[i],bomby[i],2,3);
                bomby[i]+=bombspeed;
                if (bombx[i]>(turretx-6) && bombx[i]<(turretx+5) && bomby[i]>(groundy-5))
                {
                    dying=true;
                }
                if (bomby[i]>(groundy-3))
                {
                    drawbomb[i]=false;
                    bombcount--;
                }
            }
        }
    }


    public void CheckBombs()
    {
        int whichenemy,i, lowest, whichbomb;

        if (Math.random()>0.1 || bombcount>=curmaxbombs)
            return;

        whichbomb=0;
        while(drawbomb[whichbomb] && whichbomb<curmaxbombs)
            whichbomb++;
        if (whichbomb>=curmaxbombs)
            return;

        whichenemy=(int)((double)speckcount*Math.random());
        if (whichenemy>=speckcount)
            whichenemy=speckcount-1;

        i=-1;

        while(whichenemy>=0)
        {
            i++;
            while(i<rowsofenemies*enemiesperrow && !drawenemy[i]) i++;
            whichenemy--;
        }
        if (i>=rowsofenemies*enemiesperrow)
            return;

        lowest=i;
        while(i<rowsofenemies*enemiesperrow)
        {
            if (drawenemy[i])
                lowest=i;
            i+=enemiesperrow;
        }
        bombx[whichbomb]=enemyx+(lowest%enemiesperrow)*(enemyradius+enemyxdelta);
        bomby[whichbomb]=enemyy+(lowest/enemiesperrow)*(enemyradius+enemyydelta);
        drawbomb[whichbomb]=true;
        bombcount++;
    }


    public void ShowScore()
    {
        String s;
        int i,j,y;

        goff.setFont(smallfont);
        goff.setColor(new Color(96,128,255));
        s="Score: "+score;
        goff.drawString(s,turretmarge,d.height-8);

        goff.setColor(turretcolor);
        for (j=1; j<lives; j++)
        {
            y=d.height-8;
            for (i=6; i>=0; i--)
            {
                goff.drawLine(d.width/2+j*20-i,y,d.width/2+j*20+i,y);
                y--;
            }
        }
    }


    public void ShowIntroScreen()
    {
        String s;

        goff.setFont(largefont);
        goff.setColor(textcolor1);
        s="PERANG TEMBAK";
        goff.setColor(Color.white);
        goff.drawString(s,(d.width-fmlarge.stringWidth(s))/2+2,d.height/2 - 38);
        goff.setColor(textcolor1);
        goff.drawString(s,(d.width-fmlarge.stringWidth(s))/2,d.height/2 - 40);

        goff.setFont(smallfont);
        goff.setColor(Color.white);
        s="by ariWinata";
        goff.drawString(s,(d.width-fmsmall.stringWidth(s))/2+1,d.height/2 - 9);
        goff.setColor(textcolor3);
        goff.drawString(s,(d.width-fmsmall.stringWidth(s))/2,d.height/2 - 10);

        goff.setColor(Color.white);
        s="Ayo kalahkan musuhmu, FIRE FIRE FIRE";
        goff.drawString(s,(d.width-fmsmall.stringWidth(s))/2+1,d.height/2 + 11);
        goff.setColor(textcolor3);
        goff.drawString(s,(d.width-fmsmall.stringWidth(s))/2,d.height/2 + 10);

        goff.setColor(Color.white);
        s="'s' untuk mulai";
        goff.drawString(s,(d.width-fmsmall.stringWidth(s))/2+1,d.height/2 + 31);
        goff.setColor(textcolor2);
        goff.drawString(s,(d.width-fmsmall.stringWidth(s))/2,d.height/2 + 30);

        goff.setColor(Color.white);
        s="ARAH = BERGERAK, SPASI = TEMBAK";
        goff.drawString(s,(d.width-fmsmall.stringWidth(s))/2+1,d.height/2 + 51);
        goff.setColor(textcolor2);
        goff.drawString(s,(d.width-fmsmall.stringWidth(s))/2,d.height/2 + 50);
    }


    public void paint(Graphics g)
    {
        if (goff==null && d.width>0 && d.height>0)
        {
            ii = createImage(d.width, d.height);
            goff = ii.getGraphics();
        }
        if (goff==null || ii==null)
            return;

        goff.setColor(backgnd);
        goff.fillRect(0, 0, d.width, d.height);
        goff.setColor(groundcolor);
        goff.fillRect(0,groundy,d.width,groundheight);

        if (ingame)
        {
            if (!dying)
            {
                MoveSpecks();
                DoBullet();
                CheckBombs();
                DoBombs();
            }
            DoTurret();
        }
        DrawSpecks();
        if (!ingame)
            ShowIntroScreen();
        ShowScore();

        g.drawImage(ii, 0, 0, this);
    }



    public void run()
    {
        long  starttime;
        Graphics g;

        Thread.currentThread().setPriority(Thread.MAX_PRIORITY);
        g=getGraphics();

        while(true)
        {
            starttime=System.currentTimeMillis();
            try
            {
                paint(g);
                starttime += 20;
                Thread.sleep(Math.max(0, starttime-System.currentTimeMillis()));
            }
            catch (InterruptedException e)
            {
                break;
            }
        }
    }

    public void start()
    {
        if (thethread == null)
        {
            thethread = new Thread(this);
            thethread.start();
        }
    }

    public void stop()
    {
        if (thethread != null)
        {
            thethread.stop();
            thethread = null;
        }
    }
}

