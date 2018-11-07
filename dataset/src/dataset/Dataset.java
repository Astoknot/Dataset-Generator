/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dataset;

import dataset.Database.DB;
import java.net.*;
import net.sourceforge.jpcap.*;
import net.sourceforge.jpcap.capture.*;
import net.sourceforge.jpcap.client.*;
import net.sourceforge.jpcap.net.*;
import static net.sourceforge.jpcap.net.ARPFields.ARP_ADDR_TYPE_LEN;
import static net.sourceforge.jpcap.net.ARPFields.ARP_ETH_ADDR_CODE;
import static net.sourceforge.jpcap.net.ARPFields.ARP_HW_TYPE_POS;
import static net.sourceforge.jpcap.net.ARPFields.ARP_PR_TYPE_POS;
import static net.sourceforge.jpcap.net.EthernetFields.ETH_CODE_LEN;
import static net.sourceforge.jpcap.net.EthernetFields.ETH_CODE_POS;
import static net.sourceforge.jpcap.net.ICMPFields.ICMP_CODE_LEN;
import static net.sourceforge.jpcap.net.ICMPFields.ICMP_CODE_POS;
import static net.sourceforge.jpcap.net.ICMPFields.ICMP_CSUM_LEN;
import static net.sourceforge.jpcap.net.ICMPFields.ICMP_CSUM_POS;
import static net.sourceforge.jpcap.net.ICMPFields.ICMP_SUBC_LEN;
import static net.sourceforge.jpcap.net.ICMPFields.ICMP_SUBC_POS;
import static net.sourceforge.jpcap.net.IPFields.IP_CODE_LEN;
import static net.sourceforge.jpcap.net.IPFields.IP_CODE_POS;
import static net.sourceforge.jpcap.net.TCPFields.TCP_ACK_LEN;
import static net.sourceforge.jpcap.net.TCPFields.TCP_ACK_POS;
import static net.sourceforge.jpcap.net.TCPFields.TCP_CSUM_LEN;
import static net.sourceforge.jpcap.net.TCPFields.TCP_CSUM_POS;
import static net.sourceforge.jpcap.net.TCPFields.TCP_DP_POS;
import static net.sourceforge.jpcap.net.TCPFields.TCP_FLAG_LEN;
import static net.sourceforge.jpcap.net.TCPFields.TCP_FLAG_POS;
import static net.sourceforge.jpcap.net.TCPFields.TCP_PORT_LEN;
import static net.sourceforge.jpcap.net.TCPFields.TCP_SEQ_LEN;
import static net.sourceforge.jpcap.net.TCPFields.TCP_SEQ_POS;
import static net.sourceforge.jpcap.net.TCPFields.TCP_SP_POS;
import static net.sourceforge.jpcap.net.TCPFields.TCP_WIN_LEN;
import static net.sourceforge.jpcap.net.TCPFields.TCP_WIN_POS;
import static net.sourceforge.jpcap.net.UDPFields.UDP_CSUM_LEN;
import static net.sourceforge.jpcap.net.UDPFields.UDP_CSUM_POS;
import static net.sourceforge.jpcap.net.UDPFields.UDP_DP_POS;
import static net.sourceforge.jpcap.net.UDPFields.UDP_LEN_POS;
import static net.sourceforge.jpcap.net.UDPFields.UDP_PORT_LEN;
import static net.sourceforge.jpcap.net.UDPFields.UDP_SP_POS;
import net.sourceforge.jpcap.simulator.*;
import static net.sourceforge.jpcap.simulator.HeaderGenerator.randomICMPType;
//import net.sourceforge.jcap.net.EthernetFields;
//import net.sourceforge.jpcap.tutorial;
//import net.sourceforge.jpcap.tutorial.*;
import net.sourceforge.jpcap.util.*;

/**
 *
 * @author lekhika
 */
public class Dataset {

    
    int counter; 
    /**
     * @param args the command line arguments
     */
    public Dataset(int counter) {
       this.counter=counter;
       DB db = new DB();
       HeaderGenerator h = new HeaderGenerator();
       PacketGenerator p = new PacketGenerator();
       
       System.out.println(p.generate());
       
       byte packet[]=h.generateRandomEthernetHeader();
       long u=ArrayHelper.extractLong(packet,0 , MACAddress.WIDTH); 
       String src = MACAddress.extract(0,packet);  //src
       String des = MACAddress.extract(MACAddress.WIDTH,packet);   //dst
       String ipsrc = IPAddress.extract(0,packet);
       String ipdest = IPAddress.extract(IPAddress.WIDTH,packet);
       
       System.out.println("src : " + src);
       System.out.println("dest : " + des);
       System.out.println("ipsrc : " + ipsrc);
       System.out.println("ipsrc : " +ipdest);
       
       String protocol="";

       

//       int ip = ArrayHelper.extractInteger(packet,EthernetFields.ETH_CODE_POS, EthernetFields.ETH_CODE_LEN);
//       System.out.println(ip);   //dst
       String textblob = "";
       
      int eProto = 
      ArrayHelper.extractInteger(packet, ETH_CODE_POS, ETH_CODE_LEN);

    // figure out what type of packet should be encapsulated after the 
    // newly generated ethernet header.
    switch(eProto) {
    case EthernetProtocols.IP: // encapsulate IP
      byte [] ipHeader = HeaderGenerator.generateRandomIPHeader();
      packet = ArrayHelper.join(packet, ipHeader);
      System.out.println("IP");
//      // figure out what type of protocol should be encapsulated after the 
//      // newly generated IP header.
      int ipProto = 
        ArrayHelper.extractInteger(ipHeader, IP_CODE_POS, IP_CODE_LEN);
      switch(ipProto) {
      case IPProtocols.UDP: // encapsulate UDP inside IP
        byte [] udpHeader = HeaderGenerator.generateRandomUDPHeader();
        packet = ArrayHelper.join(packet, udpHeader);
        
        //------------------------------------------------------------
      System.out.println("Src port"+ArrayHelper.extractLong(packet, UDP_SP_POS, UDP_PORT_LEN));
      System.out.println("Des port"+ArrayHelper.extractLong(packet, UDP_DP_POS, UDP_PORT_LEN));
      System.out.println("Length"+ArrayHelper.extractLong(packet, UDP_LEN_POS, UDP_PORT_LEN));
      System.out.println("CheckSum"+ArrayHelper.extractLong(packet, UDP_CSUM_POS, UDP_CSUM_LEN));
     
      //----------------------------------------------------------------
      
      textblob = "src port:"+ArrayHelper.extractLong(packet, UDP_SP_POS, UDP_PORT_LEN)
              +",dest port:"+ArrayHelper.extractLong(packet, UDP_DP_POS, UDP_PORT_LEN) 
              +",len:" +ArrayHelper.extractLong(packet, UDP_LEN_POS, UDP_PORT_LEN)
              +",csum:"+ArrayHelper.extractLong(packet, UDP_CSUM_POS, UDP_CSUM_LEN);
      System.out.println("UDP");
      protocol = "UDP";
      
      
        break;
      case IPProtocols.ICMP: // encapsulate ICMP inside IP
        byte [] icmpHeader = HeaderGenerator.generateRandomICMPHeader();
        packet = ArrayHelper.join(packet, icmpHeader);
      System.out.println("Code"+ArrayHelper.extractLong(packet,ICMP_CODE_POS, ICMP_CODE_LEN));
      System.out.println("Subcode"+ArrayHelper.extractLong(packet, ICMP_SUBC_POS, ICMP_SUBC_LEN));
      System.out.println("checksum"+ArrayHelper.extractLong(packet, ICMP_CSUM_POS, ICMP_CSUM_LEN));
      System.out.println("ICMP");
      protocol = "ICMP";
      
      //----------------------------------------------------
      
      textblob = "code:"+ArrayHelper.extractLong(packet,ICMP_CODE_POS, ICMP_CODE_LEN)
              +",subcode:"+ArrayHelper.extractLong(packet, ICMP_SUBC_POS, ICMP_SUBC_LEN)
              +",csum:"+ArrayHelper.extractLong(packet, ICMP_CSUM_POS, ICMP_CSUM_LEN);
      
        break;
      case IPProtocols.TCP: // encapsulate TCP inside IP
        byte [] tcpHeader = HeaderGenerator.generateRandomTCPHeader();
        packet = ArrayHelper.join(packet, tcpHeader);
      System.out.println("Src port"+ArrayHelper.extractLong(packet, TCP_SP_POS, TCP_PORT_LEN));
      System.out.println("Des port"+ArrayHelper.extractLong(packet, TCP_DP_POS, TCP_PORT_LEN));
      System.out.println("Sequence number "+ArrayHelper.extractLong(packet, TCP_SEQ_POS, TCP_SEQ_LEN));
      System.out.println("Checsum "+ArrayHelper.extractLong(packet, TCP_CSUM_POS, TCP_CSUM_LEN));
//      System.out.printlnh("Checsum "+ArrayHelper.extractLong(packet, TCP_CSUM_POS, TCP_CSUM_LEN));
      System.out.println("length and flag "+ ArrayHelper.extractLong(packet,TCP_FLAG_POS, TCP_FLAG_LEN));

      
      //----------------------------------------------------------------------
        textblob = "src port:"+ArrayHelper.extractLong(packet, TCP_SP_POS, TCP_PORT_LEN)
              +",dest port:"+ArrayHelper.extractLong(packet, TCP_DP_POS, TCP_PORT_LEN) 
              +",seq:" +ArrayHelper.extractLong(packet, TCP_SEQ_POS, TCP_SEQ_LEN)
              +",csum:"+ArrayHelper.extractLong(packet, TCP_CSUM_POS, TCP_CSUM_LEN)
              +",ack:" + ArrayHelper.extractLong(packet, TCP_ACK_POS, TCP_ACK_LEN)
              +",window size:"+ArrayHelper.extractLong(packet, TCP_WIN_POS, TCP_WIN_LEN);
              
        
//      db.insert(src, des, "TCP", "len");
        protocol = "TCP";
      System.out.println("TCP");
        break;
      default: 
        protocol= "--";
        textblob=null;
        break;
      }
      break;
    case EthernetProtocols.ARP:
      byte [] tcpHeader = HeaderGenerator.generateRandomTCPHeader();
        packet = ArrayHelper.join(packet, tcpHeader);
        ipdest = "Broadcast";
        src = "Broadcast";
        protocol="ARP";
        textblob=null;
        try{
            ipsrc = InetAddress.getLocalHost().toString();
            NetworkInterface network = NetworkInterface.getByName("wlp3s0");
            //System.out.println(network);
            byte[] mac = network.getHardwareAddress();
            src = mac.toString();
            //System.out.println("aaaaaaaaaaa"+src);
        }catch(Exception e){e.printStackTrace();}
        
        
        
       
        
      break;
    default:
      // if the ethernet protocol isn't known, leave the data payload empty
        protocol = "--";
        textblob = null;
      break;
    }


//       byte [] arpHeader = HeaderGenerator.generateRandomARPHeader();
//      packet = ArrayHelper.join(packet, arpHeader);
//      System.out.println(Long.toHexString(ArrayHelper.extractLong(packet,ARP_HW_TYPE_POS, ARP_ADDR_TYPE_LEN)));
//      System.out.println(Long.toHexString(ArrayHelper.extractLong(packet,ARP_PR_TYPE_POS, ARP_ADDR_TYPE_LEN)));
//      System.out.println(IPAddress.WIDTH);
//      System.out.println(MACAddress.WIDTH);
//      
//      System.out.println("ARP");

     db.insert(counter,src, des, ipsrc, ipdest, protocol, textblob);
       
    }
    
}
