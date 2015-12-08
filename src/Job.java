/**
 * Job.java
 *
 * CMSC 335 Project 4
 *
 * Alan Johnson 18 February 15 NetBeans IDE 8.0.2
 */

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.TreeMap;
import java.util.concurrent.Semaphore;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;

/**
 * Represents an job used by a <code>Creature</code> object. It also contains an
 * integer static value that represents the index to be used for the next Job
 * object created, in the case that the constructor gets an erroneous index.
 *
 * @author Alan Johnson
 * @version 1.3
 */
public class Job implements Runnable, GameElement {

    //                       ----  Class Variables  ----
    private static int nextIndex = 50_000;

    //                       ----  Instance Variables  ----
    private final int index, creatureIndex, jobTime;
    private boolean goFlag = true,
            killFlag = false,
            artifactsAcquired = false;
    private final String name;

    enum Status {

        ACQUIRING, RUNNING, SUSPENDED, WAITING, DONE
    };

    private TreeMap<String, Integer> requiredArtifacts;
    private Creature owner;
    private HashMap<String, Semaphore> semaphoreList;
    private final JobPanel jobPanel;
    private final HashMap<Semaphore, Integer> semaphoresAcquired;

    //                       ----  Constructors  ----
    /**
     *
     * @param tokens An array of <code>String[6]</code> objects, beginning with
     * "<code>t</code>"
     * @param elemMap A <code>HashMap</code> of <code>GameElement</code>s, used
     * to find the <code>Job</code>'s owner
     *
     * @return new <code>Job</code> object
     */
    static public Job newJob(ArrayList<String> tokens, HashMap<Integer, GameElement> elemMap) {

        Job job = new Job(Integer.parseInt(tokens.get(1)), //  index
                tokens.get(2), //  Name
                Integer.parseInt(tokens.get(3)), //  Creature Index
                (int) Float.parseFloat(tokens.get(4)), //  Time
                elemMap);

        int next = 7;
        job.requiredArtifacts = new TreeMap<>();

        while (tokens.size() >= next) {

            int offset = next - 2;
            
            String type = tokens.get(offset);
            
            if (type.charAt(type.length() - 1) == 's') {
                
                type = type.substring(0, type.length() - 1);
                
            }
            
            int amount = Integer.parseInt(tokens.get(offset + 1));

            job.requiredArtifacts.put(type, amount);
            
            next += 2;
        }
        
        return job;

    }  //  end Job factory method

    /**
     * @param _index Job index
     * @param _type Job type
     * @param creature index of the owning Creature object
     * @param _weight Job weight
     * @param _value Job value
     */
    private Job(int _index, String _name, int creature, int _jobTime, HashMap<Integer, GameElement> elemMap) {

        if (_index >= 50_000 && _index < 60_000 && _index >= nextIndex) {
            index = nextIndex = _index;
        } else {
            index = nextIndex++;
        }

        if (_name != null) {
            name = _name;
        } else {
            name = "no_name";
        }

        if (creature >= 20_000 && creature < 30_000) {
            creatureIndex = creature;
        } else {
            creatureIndex = 0;
        }

        if (_jobTime > 0) {
            jobTime = _jobTime;
        } else {
            jobTime = 0;
        }

        owner = (Creature) elemMap.get(creatureIndex);
        jobPanel = new JobPanel(this);
        semaphoresAcquired = new HashMap<>();

        System.out.printf("Job[%d] (%s) created.\n", index, name);

    }  //  end Treasure constructor

    
    
    //                       ----  Other Methods  ----
    
    private boolean acquireArtifactSemaphores() {
        
        if (semaphoreList.size() < 1) return false;

        for (Entry<String, Integer> entry : requiredArtifacts.entrySet()) {

            String artifactName = entry.getKey();
            int artifactReq = entry.getValue();
            if (artifactReq < 1) {
                continue;
            }

            Semaphore semaphore = semaphoreList.get(artifactName);
            
            if (semaphore == null) return false;

            synchronized (semaphore) {

                try {
                    
                    if (semaphore.availablePermits() >= artifactReq)
                        semaphore.acquire(artifactReq);
                    
                    else throw new Exception();  //  new 
                    
                    semaphoresAcquired.put(semaphore, artifactReq);
                    
                } catch (Exception e) {
                    releaseArtifactSemaphores();
                    return false;
                }
                
            }  //  end synchronized

        }  //  for each artifact type, acquire enough

        artifactsAcquired = true;
        return true;

    }  //  end method acquireArtifactSemaphores()

    private void releaseArtifactSemaphores() {

        for (Entry<Semaphore, Integer> entry : semaphoresAcquired.entrySet()) {

            Semaphore semaphore = entry.getKey();
            int permits = entry.getValue();
            
            synchronized (semaphore) {
            
                try {
                    semaphore.release(entry.getValue());
                }
                
                catch (Exception e) { }

                
            }  //  end synchronized on semaphore

        }  //  for each artifact type, acquire enough

        semaphoresAcquired.clear();
        artifactsAcquired = false;

    }  //  end method acquireArtifactSemaphores()

    /**
     *
     * @return
     */
    @Override
    public String getType() {
        return name;
    }  //  end method getType()

    /**
     * Returns the index that identifies the <code>Creature</code> object that
     * owns this <code>Treasure</code>.
     *
     * @return the index of the <code>Creature</code> owner
     */
    public int getCreatureIndex() {
        return creatureIndex;
    }  //  end method getCreatureIndex()

    /**
     * Returns the value of <code>index</code>.
     *
     * @return the value of the <code>index</code>
     */
    @Override
    public int getIndex() {
        return index;
    }  //  end method getIndex()

    /**
     * Returns the value of <code>name</code>.
     *
     * @return the value of <code>name</code>
     */
    @Override
    public String getName() {
        return name;
    }  //  end getName() method

    /**
     *
     * @return
     */
    public Creature getOwner() {
        return owner;
    }  //  end method getOwner()

    /**
     *
     * @return
     */
    public JPanel getPanel() {
        return jobPanel;
    }  //  end method getPanel()

    /**
     *
     */
    @Override
    public void run() {

        long time = System.currentTimeMillis();
        long startTime = time;
        long stopTime = time + 1000 * jobTime;
        double duration = stopTime - time;

        if (owner != null) {

            synchronized (owner.getOwner()) {

                while (!acquireArtifactSemaphores()) {
                    
                    jobPanel.setStatus(Status.ACQUIRING);
                    try {
                        owner.wait();
                    } catch (Exception e) {
                    }
                }  //  end while waiting for artifacts

            }  //  synchronized on Party
            
            synchronized (owner) {

                while (owner.isBusy) {
                    
                    jobPanel.setStatus(Status.WAITING);
                    try {
                        owner.wait();
                    } catch (Exception e) {
                    }
                }  //  end while waiting for Creature to be free
                owner.isBusy = true;

                owner.getOwner().updateArtifactPanels();
                
            }  //  end synchronized

            while (time < stopTime && !killFlag) {
                try {
                    Thread.sleep(100);
                } catch (Exception e) {
                }
                if (goFlag) {
                    jobPanel.setStatus(Status.RUNNING);
                    time += 100;
                    jobPanel.progressBar.setValue((int) (((time - startTime) / duration) * 100));
                } else {
                    jobPanel.setStatus(Status.SUSPENDED);
                }  //  end if goFlag
            }  //  end while time remaining

            jobPanel.progressBar.setValue(100);
            jobPanel.setStatus(Status.DONE);

            releaseArtifactSemaphores();
            owner.getOwner().updateArtifactPanels();
            
            synchronized (owner) {
                
                owner.isBusy = false;
                owner.notifyAll();
            }  // end synchronized

        }  //  if owner isn't null

    }  //  end method run()

    /**
     *
     */
    private void setKillFlag() {
        killFlag = true;
    }  //  end method setKillFlag()

    /**
     *
     * @param _owner
     */
    public void setOwner(Creature _owner) {
        if (_owner != null) {
            owner = _owner;
        }
    }  //  end method setOwner()

    /**
     *
     * @param semaphoreList
     */
    public void startJob(HashMap<String, Semaphore> s) {

        semaphoreList = s;

        (new Thread(this, name)).start();

    }

    /**
     *
     */
    private void toggleGoFlag() {
        goFlag = !goFlag;
    }  //  end method toggleGoFlag()

    /**
     *
     * @return String representation
     */
    @Override
    public String toString() {
        return name;
    }  //  end method toString()

    /**
     *
     */
    private class JobPanel extends JPanel {

        private static final long serialVersionUID = 1L;

        JButton goButton = new JButton("Start"),
                killButton = new JButton("Cancel");
        JProgressBar progressBar;
        Job owner;

        Status status = Status.SUSPENDED;

        /**
         *
         * @param o
         */
        public JobPanel(Job o) {
            super();
            if (o != null) {
                owner = o;
            }
            FlowLayout layout = new FlowLayout();
            setSize(400, 40);

            setLayout(layout);
            JPanel panel = new JPanel();
            JLabel label = new JLabel(String.format("%s (%s)", owner.name, owner.owner));
            this.setBorder(BorderFactory.createTitledBorder(owner.owner.getOwner().getName()));
            label.getInsets().set(1, 1, 1, 1);
            label.setPreferredSize(new Dimension(130, 20));
            panel.add(label);
            panel.setPreferredSize(new Dimension(140, 30));
            add(panel);

            panel = new JPanel();
            progressBar = new JProgressBar();
            progressBar.setStringPainted(true);
            progressBar.setPreferredSize(new Dimension(50, 20));
            panel.add(progressBar);
            panel.setPreferredSize(new Dimension(60, 30));
            add(panel);

            panel = new JPanel();
            goButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    owner.toggleGoFlag();
                }
            });
            panel.add(goButton);
            goButton.setPreferredSize(new Dimension(80, 20));
            goButton.setMargin(new Insets(1, 1, 1, 1));
            panel.setPreferredSize(new Dimension(85, 30));
            add(panel);

            panel = new JPanel();
            killButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    owner.setKillFlag();
                    killButton.setVisible(false);
                }
            });
            panel.add(killButton);
            killButton.setPreferredSize(new Dimension(50, 20));
            killButton.setMargin(new Insets(1, 1, 1, 1));
            panel.setPreferredSize(new Dimension(60, 30));
            add(panel);

            
        }  //  end Job constructor

        /**
         *
         * @param st
         */
        void setStatus(Status st) {

            switch (st) {
                case ACQUIRING:
                    goButton.setBackground(Color.yellow);
                    goButton.setText("Acquiring");
                    break;

                case RUNNING:
                    goButton.setBackground(Color.green);
                    goButton.setText("Running");
                    break;

                case SUSPENDED:
                    goButton.setBackground(Color.yellow);
                    goButton.setText("Suspended");
                    break;

                case WAITING:
                    goButton.setBackground(Color.orange);
                    goButton.setText("Waiting");
                    break;

                case DONE:
                    goButton.setBackground(Color.red);
                    goButton.setText("Done");
                    killButton.setVisible(false);
                    break;
            }  //  end switch
        }  //  end method setStatus()
    }  //  end class JobPanel
}  //  end class Job
