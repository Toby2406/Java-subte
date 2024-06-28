package ar.edu.uns.cs.ed.proyectos.subtes.routing.proyecto;

import java.util.Iterator;


import ar.edu.uns.cs.ed.proyectos.subtes.entities.Estacion;
import ar.edu.uns.cs.ed.proyectos.subtes.entities.Linea;
import ar.edu.uns.cs.ed.proyectos.subtes.entities.LineaSubte;
import ar.edu.uns.cs.ed.proyectos.subtes.routing.Router;
import ar.edu.uns.cs.ed.proyectos.subtes.routing.RouterSubte.PrecomputationBuilder;
import ar.edu.uns.cs.ed.proyectos.subtes.routing.util.Par;
import ar.edu.uns.cs.ed.proyectos.subtes.routing.Viaje;
import ar.edu.uns.cs.ed.proyectos.subtes.routing.Viaje.ForwardsMiddleEndBuilder;
import ar.edu.uns.cs.ed.proyectos.subtes.routing.ViajeSubte;
import ar.edu.uns.cs.ed.proyectos.subtes.routing.ViajeSubte.BackwardsMiddleStartBuilder;
import ar.edu.uns.cs.ed.proyectos.subtes.tdas.exceptions.InvalidKeyException;
import ar.edu.uns.cs.ed.proyectos.subtes.tdas.tdadiccionario.DiccionarioHashAbierto;
import ar.edu.uns.cs.ed.proyectos.subtes.tdas.tdadiccionario.Dictionary;
import ar.edu.uns.cs.ed.proyectos.subtes.tdas.tdadiccionario.Entry;
import ar.edu.uns.cs.ed.proyectos.subtes.tdas.tdalista.ListaDoblementeEnlazada;
import ar.edu.uns.cs.ed.proyectos.subtes.tdas.tdalista.PositionList;
import ar.edu.uns.cs.ed.proyectos.subtes.tdas.tdamapeo.*;
import ar.edu.uns.cs.ed.proyectos.subtes.tdas.tdapila.*;

/* TODO Proyecto_Tarea_1: Renombrar la clase ProyectoEjemplo siguiendo 
 * la siguiente convención: Proyecto_LU1_LU2
 * Usar Refactor > Rename en eclipse (de manera de que cambie también 
 * el nombre del archivo y las referencias existentes)
 */
public class Proyecto_137841_130621 implements Proyecto{
	// TODO Proyecto_Tarea_2: Declarar acá las estructuras de datos/TDAs necesarias
		protected Dictionary<Estacion,Linea> diccionario = new DiccionarioHashAbierto<Estacion,Linea>();
		protected Dictionary<Linea,Par<Linea,Estacion>> combinaciones = new DiccionarioHashAbierto<Linea,Par<Linea,Estacion>>();
		protected Dictionary<Linea,Estacion> diccionarioViaje = new DiccionarioHashAbierto<Linea,Estacion>();
		@Override
		public String getComision() {
			// TODO Proyecto_Tarea_1: Ajustar los datos de la comisión
			String alumno1 = "Luna, Tobías";
			String lu1 = "137841";
			String alumno2 = "Quiroga Buceta, Manuel";
			String lu2 = "130621";
			return alumno1+" (LU: "+lu1+") - "+alumno2+" (LU: "+lu2+")";
		}
		
		@Override
		public void precomputarLineasDeEstaciones(PrecomputationBuilder pb) {		
			/* TODO Proyecto_Tarea_3: Computar la(s) línea(s) a las que 
			 * pertenece cada estación y cargar la estructura elegida
			 */
		
			//Para cada Estación, calcular y almacenar en una estructura adecuada las Líneas a
					//las cuales pertenece la estación.
					diccionario = new DiccionarioHashAbierto<Estacion,Linea>(); //Creo un nuevo diccionario  de estacaciones con su linea

					//Iterable de lienas creadas.
					for(Linea l: pb.getAllLineas()){
						//Genero una busqueda de todas las estaciones e la liena ls.
						Estacion cursor = l.getCabeceraInicial();
						while(cursor != null){
							try {
								diccionario.insert(cursor, l);
								cursor = l.viajarHaciaCabeceraFinal(cursor);
							} catch (InvalidKeyException e) {
								e.printStackTrace();
							}
						}
					}
		}
		
		@Override
		public Iterable<Linea> getLineas(Estacion estacion) {
			
			/* TODO Proyecto_Tarea_3: A partir de la precomptación de Lineas 
			 * de Estaciones, implementar este método para que devuelva las 
			 * líneas a las que pertenece la estación dada
			 */
			
			PositionList<Linea> lista = new ListaDoblementeEnlazada<Linea>(); //creo una lista
			
			try {
				//busco todas las lineas que este asociadas a la estacion que me pide
				for(Entry<Estacion, Linea> e : diccionario.findAll(estacion)) {
					lista.addLast(e.getValue()); // y la añado a lista 
				}
				
			} catch (InvalidKeyException e) {
				e.printStackTrace();
			}
			
			return lista;
		}
		
		@Override
		public void precomputarCombinaciones(PrecomputationBuilder pb) {

			/* TODO Proyecto_Tarea_4: Computar las combinaciones entre 
			 * líneas y cargar la estructura elegida
			 */
			precomputarLineasDeEstaciones(pb);
			
			combinaciones = new DiccionarioHashAbierto<Linea,Par<Linea,Estacion>>();
			PositionList<Par<Linea,Estacion>> ListaPar = new ListaDoblementeEnlazada<Par<Linea,Estacion>>();
					
			//Carga de elemetos en lista
			for(Entry<Estacion,Linea> entrada : diccionario.entries()) {
				Par<Linea,Estacion> p = new Par<Linea,Estacion>(entrada.getValue(),entrada.getKey());
				ListaPar.addLast(p);			
			}
			
			//Carga de elementos en diccionario combinaciones
			for(Par<Linea,Estacion> p : ListaPar)
			{
				for(Par<Linea,Estacion> p2 : ListaPar)
				{
					if(!(p.getFirst().equals(p2.getFirst())) && (p.getSecond().equals(p2.getSecond())))
					{
						try {
							combinaciones.insert(p.getFirst(), p2);
						} catch (InvalidKeyException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}
			}
		}
				
		@Override
		public Iterable<Par<Linea,Estacion>> getCombinaciones(Linea linea) {
			
			/* TODO Proyecto_Tarea_4: A partir de la precomptación de Combinaciones, 
			 * implementar este método para que devuelva un iterable con las 
			 * combinaciones (i.e. pares de línea a combinar y estación de combinación) 
			 * de la línea dada
			 */
		
			PositionList<Par<Linea,Estacion>> LineaCEstacion =  new ListaDoblementeEnlazada<Par<Linea,Estacion>>();
			
				try {
					for(Entry<Linea,Par<Linea,Estacion>> lineasCombinadas : combinaciones.findAll(linea)){
							LineaCEstacion.addLast(lineasCombinadas.getValue());
						}
					}	
				 catch (InvalidKeyException e) {
					e.printStackTrace();
				}
			
			return LineaCEstacion;
			
		}
		
		@Override
		public Viaje viajar(Estacion origen, Estacion destino, Router router) {
			
			/* TODO Proyecto_Tarea_5: Resolver la tarea principal del proyecto: 
			 * el cálculo y creación de viajes entre las estaciones dadas. 
			 * Utilizando las estructuras de precomputación, y las estructuras 
			 * auxiliares que considere necesarias construya un Viaje que vaya
			 * de origen a destino utilizando la red de subtes cargada.
			 */
			
			//Si viaja sobre la misma linea
			Viaje v = null;
					
			//Completo diccionarioViaje con las lineas y estaciones del router
			RellenarDiccionarioviaje(router);
					
			//Busqueda de lineas
			Linea LOrige = BuscarLinea(origen);
			Linea LDestino = BuscarLinea(destino);
			
			
			
			if(LOrige.equals(LDestino)){ //Si el viaje es en la misma linea
				
				v = ViajeSubte.getForwardsBuilder()
						.setOrigen(origen, LDestino)
						.agregarDireccion(destino, buscarCabeceraDeAaB(origen, destino, LDestino), LDestino)
						.setDestinoAndBuild(destino, LDestino);
			}
			else {
				PilaConLista<Par<Linea,Estacion>> lista = new PilaConLista<Par<Linea,Estacion>>();
				Par<Linea,Estacion> p = new Par<Linea,Estacion>(LDestino,destino);
				lista.push(p);
				
				LineasARecorrer(LOrige, LDestino, router, lista,p);
				ForwardsMiddleEndBuilder aux = ViajeSubte.getForwardsBuilder().setOrigen(origen, LOrige);

				Estacion estacionAux = origen;
				Linea lineaAux = LOrige;
				Estacion combinacion;
				try {
					combinacion = lista.top().getSecond();
					aux = aux.agregarDireccion(combinacion, buscarCabeceraDeAaB(origen, combinacion, LOrige),LDestino);
					if(combinacion.equals(destino)) //Si la pila solo cuenta con el elemento de destino
					{
						v = ViajeSubte.getForwardsBuilder()
								.setOrigen(origen, LDestino)
								.agregarDireccion(destino, buscarCabeceraDeAaB(origen, destino, LDestino), LDestino)
								.setDestinoAndBuild(destino, LDestino);
					}else {
						while(!lista.isEmpty())
						{
							Linea li = lista.top().getFirst() ;//Linea de la combinacion
							combinacion = lista.pop().getSecond();
							
							if(!li.equals(LDestino))
							{
								if(!LOrige.equals(LDestino))
								{
									aux = aux.agregarDireccion(combinacion, buscarCabeceraDeAaB(estacionAux, combinacion, lineaAux), li);
								}
								estacionAux = combinacion;
								lineaAux = li;
								aux = aux.agregarCombinacion(li);
							}else
							{
								if(!LOrige.equals(LDestino))
								{
									if(!combinacion.equals(destino))
									{
										aux = aux.agregarDireccion(destino, buscarCabeceraDeAaB(combinacion, destino, li), LDestino);
									}
								}
								v = aux.setDestinoAndBuild(destino, LDestino);
							}
						
						}
					}
				} catch (EmptyStackException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				

			}				
			return v;
		}
		

		
		protected Estacion buscarCabeceraDeAaB(Estacion A, Estacion B, Linea l )
		{
			//Busca la dirrecion de la cabecera pra viajar de A a B por l.
			//Arranco desde A en direccion a la cabecera final, obtengo la siguiente estacion. comparo sies b
			// sin sigo, si encunetro B se viaja por cabecera final sino por cabecera inicial.
			
			Estacion resultado = l.getCabeceraInicial();
			
			Estacion cursor = A;
			
			while(cursor != null && resultado == l.getCabeceraInicial())
			{
				if(cursor.equals(B))
				{
					resultado = l.getCabeceraFinal();
				}
				cursor = l.viajarHaciaCabeceraFinal(cursor);
			}
			
			return resultado;
		}
		protected void RellenarDiccionarioviaje(Router r)
		{
			diccionarioViaje = new DiccionarioHashAbierto<Linea, Estacion>();
			for(Estacion estaciones : r.getAllEstaciones())
			{
				for(Linea lineas : r.getLineas(estaciones))
				{
					try {
						diccionarioViaje.insert(lineas, estaciones);
					} catch (InvalidKeyException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}
		
		protected Linea BuscarLinea(Estacion e)
		{
			//Buscar dentro de diccionario de viaje la linea correspondiente a la estacion
			Linea resultado = null;
			Iterator<Entry<Linea,Estacion>> it = diccionarioViaje.entries().iterator();
		
			while(it.hasNext() && resultado == null)
			{ 
				Entry<Linea,Estacion> entrada = it.next();
				if(entrada.getValue().equals(e))
				{
					resultado = entrada.getKey();
				}
			}
			
			return resultado;
		}
		
		public boolean EstanCombinadas(Linea LO, Linea LD, Router r)
		{//Controla si las lineas estan combinadas
			boolean resultado = false;
			
			Iterator<Par<Linea,Estacion>> it = r.getCombinaciones(LO).iterator();
			if(LO.equals(LD))
			{
				resultado= true;
			}
			while(it.hasNext() && !resultado)
			{
				Par<Linea,Estacion> p = it.next();
				
				if(p.getFirst().equals(LD))
				{
					resultado = true;
				}
			}
			return resultado;
		}
		
		
		protected Estacion EstacionCombinada(Linea origen, Linea destino, Router r)
		{//RETORNA LA ESTACION DONDE COLICIONAN AMBAS LINEAS	
			Estacion resultado = null;
			
			Iterator<Par<Linea,Estacion>> it = r.getCombinaciones(origen).iterator();
			
			while(it.hasNext() && resultado == null)
			{
			
				Par<Linea,Estacion> p = it.next();
				
				if(p.getFirst().equals(destino))
				{
					resultado = p.getSecond();
				}
			}
			
			return resultado;
		}
		
		protected void LineasARecorrer(Linea origen, Linea destino, Router r, Stack<Par<Linea,Estacion>> lista,Par<Linea,Estacion> par)
		{
			//SE GENERA UNA LISTA CON TODAS LAS LINEAS QUE SE DEBEN RECORRER PARA LLEGAR A DESTINO
			//obtenemos las combinaciones de destino que tengan relacion con origen.
				if(!origen.equals(destino)) {
					try {
						if(!lista.top().equals(par))
						{
							lista.push(par);; 
						}
					} catch (EmptyStackException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				
					
					if(!r.getCombinaciones(destino).iterator().hasNext())
					{
						while(!lista.isEmpty())
						{
							try {
								lista.pop();
							} catch (EmptyStackException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
					}
					
					if(EstanCombinadas(origen, destino, r))
					{
						origen = destino;
					}else {
						for(Par<Linea,Estacion> p : r.getCombinaciones(destino))
						{
							LineasARecorrer(origen, p.getFirst(), r, lista,p);
						}
					}
				}
				
		}
		
	}
