package equalsnull.ModulesWorker;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
public class Main extends JavaPlugin {
    @Override
    public void onEnable() {
    	
    }
    @Override
    public void onDisable() {
    	
    }
    
    public boolean onCommand(CommandSender sender, Command cmd,String label,String[] args) {
    	if(label.equalsIgnoreCase("disable") || label.equalsIgnoreCase("deactivate")) {
    		if(hasRight(sender)) {
    			Bukkit.getPluginManager().disablePlugin(Bukkit.getPluginManager().getPlugin(args[0]));
    			Bukkit.getScheduler().cancelTasks(Bukkit.getPluginManager().getPlugin(args[0]));
    			sender.sendMessage("Done!");
    		}
    	}else
    	if(label.equalsIgnoreCase("enable") || label.equalsIgnoreCase("activate")) {
    		if(hasRight(sender)) {
    			Bukkit.getPluginManager().enablePlugin(Bukkit.getPluginManager().getPlugin(args[0]));
    			sender.sendMessage("Done!");
    		}
    	}else
    	if(label.equalsIgnoreCase("reload")) {
    		if(hasRight(sender)) {
    			Bukkit.getPluginManager().disablePlugin(Bukkit.getPluginManager().getPlugin(args[0]));
    			Bukkit.getScheduler().cancelTasks(Bukkit.getPluginManager().getPlugin(args[0]));
    			Bukkit.getPluginManager().enablePlugin(Bukkit.getPluginManager().getPlugin(args[0]));
    			sender.sendMessage("Done!");
    		}
    	}else
    		if(label.equalsIgnoreCase("download") || label.equalsIgnoreCase("get")) {
        		if(hasRight(sender)) {
        			String link = args[0];
        			String name = "plugins/"+link.substring(link.lastIndexOf("/")+1, link.length());
        			System.out.println(link);
        			System.out.println(name);
        			if(writeFileDataToFile(link,name)) {
        				sender.sendMessage("Downloaded sucessfully");
        			}else {
        				sender.sendMessage("Error!");
        			}
        			
        		}
        	}
    	return false;
    }
    public boolean hasRight(CommandSender sender) {
    	if(sender instanceof Player) {
    		return ((Player)sender).isOp();
    	}else {
    		return true;
    	}
    }
    public void downloadFile(String fromUrl, String localFileName) throws IOException {
        File localFile = new File(localFileName);
        if (localFile.exists()) {
            localFile.delete();
        }
        localFile.createNewFile();
        URL url = new URL(fromUrl);
        OutputStream out = new BufferedOutputStream(new FileOutputStream(localFileName));
        URLConnection conn = url.openConnection();
        String encoded = Base64.getEncoder().encodeToString(("username"+":"+"password").getBytes(StandardCharsets.UTF_8));  //Java 8
        conn.setRequestProperty("Authorization", "Basic "+ encoded);
        InputStream in = conn.getInputStream();
        byte[] buffer = new byte[1024];

        int numRead;
        while ((numRead = in.read(buffer)) != -1) {
            out.write(buffer, 0, numRead);
        }
        if (in != null) {
            in.close();
        }
        if (out != null) {
            out.close();
        }
    }
    private boolean writeFileDataToFile(String fromUrl, String localFileName) {
    	File localFile = new File(localFileName);
        if (localFile.exists()) {
            localFile.delete();
        }

        OutputStream output = null;
        InputStream input = null;
        
        try {
        	localFile.createNewFile();
        	URLConnection conn = new URL(fromUrl).openConnection();
        	conn.connect();
        	output = new FileOutputStream(localFile);
        	input = conn.getInputStream();
        	byte[] fileChunk = new byte[8*1024];
        	int bytesRead;
        	while ((bytesRead = input.read(fileChunk )) != -1) {
        		output.write(fileChunk , 0, bytesRead);
        	}
          
          return true;
        } catch (IOException e) {
          //System.out.println("Receiving file at " + conn.getURL().toString() + " failed");
        }
        return false;
    }
}
