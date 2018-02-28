package tp.fil.main;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.common.util.TreeIterator;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.xmi.impl.EcoreResourceFactoryImpl;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceFactoryImpl;
import org.eclipse.gmt.modisco.java.BodyDeclaration;
import org.eclipse.gmt.modisco.java.ClassDeclaration;
import org.eclipse.gmt.modisco.java.FieldDeclaration;
import org.eclipse.gmt.modisco.java.emf.JavaPackage;

public class DataComputation {
	
	public static void main(String[] args) {
		try {
			Resource javaModel;
			Resource dataModel;
			Resource dataMetamodel;
			
			ResourceSet resSet = new ResourceSetImpl();
			resSet.getResourceFactoryRegistry().
				getExtensionToFactoryMap().
				put("ecore", new EcoreResourceFactoryImpl());
			resSet.getResourceFactoryRegistry().
				getExtensionToFactoryMap().
				put("xmi", new XMIResourceFactoryImpl());
			
			JavaPackage.eINSTANCE.eClass();
			
			dataMetamodel = resSet.createResource(URI.createFileURI("src/tp/fil/resources/Data.ecore"));
			dataMetamodel.load(null);
			EPackage.Registry.INSTANCE.put("http://data", 
					dataMetamodel.getContents().get(0));
			
			javaModel = resSet.createResource(URI.createFileURI("../PetStore/PetStore_java.xmi"));
			javaModel.load(null);
			
			dataModel = resSet.createResource(URI.createFileURI("data/PetStore_data_fromJava.xmi"));
			
			EPackage dataMetamodelRoot = (EPackage) dataMetamodel.getContents().get(0);
			
			EClass classWrapperModel = (EClass) dataMetamodelRoot.getEClassifier("Model");
			EObject objWrapperModel = dataMetamodelRoot.getEFactoryInstance().create(classWrapperModel);
			
			List<EObject> classes = new ArrayList<>();
			TreeIterator<EObject> iterator;
			iterator = javaModel.getAllContents();
			while(iterator.hasNext()) {
				EObject currentModelElement = iterator.next();
				if( currentModelElement.eClass().getName().equals("ClassDeclaration") ) {
					ClassDeclaration classDeclaration = (ClassDeclaration) currentModelElement;
					if(classDeclaration.getPackage() != null
							&& classDeclaration.getPackage().getName().contentEquals("model")
							&& classDeclaration.getPackage().getPackage() != null
							&& classDeclaration.getPackage().getPackage().getName().contentEquals("petstore")) {
						String name = classDeclaration.getName();
						EClass classWrapperClass = (EClass) dataMetamodelRoot.getEClassifier("Class");
						EObject objWrapperClass = dataMetamodelRoot.getEFactoryInstance().create(classWrapperClass);
						objWrapperClass.eSet(classWrapperClass.getEStructuralFeature("Name"), name);
						
						List<EObject> attributes = new ArrayList<>();
						EClass classWrapperAttribute = (EClass) dataMetamodelRoot.getEClassifier("Attribute");
						for(BodyDeclaration bodyDeclaration : classDeclaration.getBodyDeclarations()) {
							if(bodyDeclaration.eClass().getName().contentEquals("FieldDeclaration")) {
								FieldDeclaration fieldDeclaration = (FieldDeclaration) bodyDeclaration;
								String attrName = fieldDeclaration.getFragments().get(0).getName();
								String attrType = fieldDeclaration.getType().getType().getName();
								EObject objWrapperAttribute = dataMetamodelRoot.getEFactoryInstance().create(classWrapperAttribute);
								objWrapperAttribute.eSet(classWrapperAttribute.getEStructuralFeature("Name"), attrName);
								objWrapperAttribute.eSet(classWrapperAttribute.getEStructuralFeature("Type"), attrType);
								attributes.add(objWrapperAttribute);
							}
						}
						
						objWrapperClass.eSet(classWrapperClass.getEStructuralFeature("Attributes"), attributes);
						classes.add(objWrapperClass);
					}
				}
			}
			
			objWrapperModel.eSet(classWrapperModel.getEStructuralFeature("Classes"), classes);
			dataModel.getContents().add(objWrapperModel);
			
			dataModel.save(null);
			
			javaModel.unload();
			dataModel.unload();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}
