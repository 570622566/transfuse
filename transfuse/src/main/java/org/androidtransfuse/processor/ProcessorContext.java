package org.androidtransfuse.processor;

import com.google.inject.assistedinject.Assisted;
import org.androidtransfuse.analysis.ModuleProcessor;
import org.androidtransfuse.model.manifest.Manifest;
import org.androidtransfuse.model.r.RResource;

import javax.inject.Inject;
import javax.inject.Provider;

/**
 * @author John Ericksen
 */
public class ProcessorContext {

    private Manifest manifest;
    private Manifest sourceManifest;
    private RResource rResource;
    private ModuleProcessor moduleProcessor;

    @Inject
    public ProcessorContext(@Assisted RResource rResource, @Assisted Manifest manifest, @Assisted ModuleProcessor moduleProcessor, Provider<Manifest> manifestProvider) {
        this.manifest = manifest;
        this.rResource = rResource;
        this.moduleProcessor = moduleProcessor;
        sourceManifest = manifestProvider.get();
    }

    public Manifest getManifest() {
        return manifest;
    }

    public RResource getRResource() {
        return rResource;
    }

    public ModuleProcessor getModuleProcessor() {
        return moduleProcessor;
    }

    public Manifest getSourceManifest() {
        return sourceManifest;
    }
}
